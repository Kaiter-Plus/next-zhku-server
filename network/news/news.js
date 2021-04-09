const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 新闻路径
const baseUrl = 'https://news.zhku.edu.cn'

// 导出一个获取结果的一个函数
module.exports = params => {
  // 拼接链接
  const url = `${baseUrl}/${params.info}/${params.type}/${params.no}`
  // 新闻列表
  const news = {}
  return new Promise((resolve, reject) => {
    request({
      url,
      headers: {
        Host: 'news.zhku.edu.cn',
        Referer: 'https://news.zhku.edu.cn/',
      },
    })
      .then(res => {
        const $ = cheerio.load(res)
        // 判断内容是否已经被注销
        if (!$('body').children().get(0)) {
          resolve({
            title: '内容已经被注销，请切换其它新闻查看',
            date: '',
            author: '',
            content: [
              {
                id: '1',
                tag: 'p',
                text: '该内容已经被注销了，请切换其它新闻查看',
              },
            ],
          })
        }
        // 获取主要容器
        const container = $('.leftcontent')
        // 获取标题
        const title = $('h1', container)
        news.title = title.text()
        // 获取发布信息
        const infomation = $('.tit', container)
          .text()
          .split(/\s{2}/g)
          .filter(v => {
            return v.indexOf('发布时间') > -1 || v.indexOf('作者') > -1
          })
          .map(v => {
            return v.split('：')[1].trim()
          })
        // 发布时间
        news.date = infomation[0]
        // 作者
        news.author = infomation[1]
        // 获取主要内容
        news.content = []
        let content = $('div', container).eq(1)
        if (content.children().get(0).tagName.toLowerCase() === 'div') {
          content = content.children().children()
        } else {
          content = content.children()
        }
        // 遍历添加内容
        Array.prototype.forEach.call(content, v => {
          const md5 = crypto.createHash('md5')
          if ($(v).attr('class') && $(v).attr('class').indexOf('img') > -1 && $(v).children().get(0)) {
            // 图片
            let img
            if ($(v).children().eq(0).attr('src')) {
              img = $(v).children()
            } else {
              img = $(v).children().children()
            }
            news.content.push({
              id: md5.update(img.attr('src') + '1').digest('hex'),
              tag: img[0].tagName.toLowerCase(),
              src: img.attr('src').indexOf('http') > -1 ? img.attr('src') : baseUrl + img.attr('src'),
            })
          } else if (v.tagName.toLowerCase() === 'table') {
            // 表格
            news.content.push({
              id: md5.update($(v).text()).digest('hex'),
              tag: v.tagName.toLowerCase(),
              text: $(v).html(),
            })
          } else {
            if ($(v).children().get(0) && $(v).children().get(0).tagName === 'img') {
              // 没有设置类名的图片
              news.content.push({
                id: md5.update($(v).children().eq(0).attr('src') + '1').digest('hex'),
                tag: $(v).children().get(0).tagName.toLowerCase(),
                src:
                  $(v).children().eq(0).attr('src').indexOf('http') > -1
                    ? $(v).children().eq(0).attr('src')
                    : baseUrl + $(v).children().eq(0).attr('src'),
              })
            } else {
              // 其它
              news.content.push({
                id: md5.update($(v).text()).digest('hex'),
                tag: $(v).get(0).tagName.toLowerCase(),
                text: $(v).text(),
              })
            }
          }
        })
        // 获取最近更新
        news.recentUpdates = {}
        // 获取最近更新容器
        const recentUpdatesContainer = $('.ricontent')
        // 获取最近更新主标题
        const recentUpdatesTitle = $('.rititle', recentUpdatesContainer).eq(0).text()
        news.recentUpdates.title = recentUpdatesTitle
        // 获取最近更新新闻
        news.recentUpdates.lastestNews = []
        const LastestNewsList = $('.ricontent-li', recentUpdatesContainer)
        Array.prototype.forEach.call(LastestNewsList, v => {
          const a = $(v).find('a')
          const md5 = crypto.createHash('md5')
          let href = a.attr('href')
          // 排除外网链接
          if (/http.*:\/\//g.test(href)) {
            return
          }
          if (href.split('/').length === 1) {
            href = `${params.info}/${params.type}/${href}`
          } else {
            href = href.replace(/\.\./g, 'info')
          }
          news.recentUpdates.lastestNews.push({
            id: md5.update(href).digest('hex'),
            href: href,
            title: a.attr('title'),
          })
        })
        resolve(news)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}
