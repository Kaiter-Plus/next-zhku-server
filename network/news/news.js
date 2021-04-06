const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 新闻路径
const baseUrl = 'https://news.zhku.edu.cn'

// 导出一个获取结果的一部函数
module.exports = params => {
  // 新闻列表
  const news = {}
  return new Promise((resolve, reject) => {
    request(`${baseUrl}/${params.info}/${params.type}/${params.no}`)
      .then(res => {
        const $ = cheerio.load(res)
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
        // 遍历添加北荣
        Array.prototype.forEach.call(content, v => {
          const md5 = crypto.createHash('md5')
          // 图片
          if ($(v).attr('class') && $(v).attr('class').indexOf('img') > -1) {
            let img
            if ($(v).children().get(0) && $(v).children().get(0).tagName === 'img') {
              img = $(v).children()
            } else {
              img = $(v).children().children()
            }
            news.content.push({
              id: md5.update(img.attr('src') + '1').digest('hex'),
              tag: img.get(0).tagName.toLowerCase(),
              src: img.attr('src').indexOf('http') > -1 ? img.attr('src') : baseUrl + img.attr('src'),
            })
          } else if (v.tagName.toLowerCase() === 'table') {
            news.content.push({
              id: md5.update($(v).text()).digest('hex'),
              tag: v.tagName.toLowerCase(),
              text: $(v).html(),
            })
          } else {
            news.content.push({
              id: md5.update($(v).text()).digest('hex'),
              tag: $(v).get(0).tagName.toLowerCase(),
              text: $(v).text(),
            })
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
