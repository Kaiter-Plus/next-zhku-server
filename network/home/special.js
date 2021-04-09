const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')
const redis = require('../../redis/index.js')

// 新闻网初始路径路径
const baseUrl = 'https://news.zhku.edu.cn/'

// 获取专题标题
function getSpecialTitle(url) {
  return new Promise((resolve, reject) => {
    // 判断 redis 中是否有数据,有则直接使用
    redis.get('specialTitles', result => {
      if (result) {
        resolve(result)
      } else {
        // 新闻列表
        const specialTitles = []
        request({
          url: baseUrl + url,
          headers: {
            Host: 'news.zhku.edu.cn',
            Referer: 'https://news.zhku.edu.cn/',
          },
        })
          .then(res => {
            // 解析文档
            const $ = cheerio.load(res)
            // 获取专题标题
            const titles = $('li.snav-li')
            // 遍历添加数据
            for (let i = 0; i < 4; i += 1) {
              const a = $(titles[i]).find('a')
              const href = a.attr('href')
              // 新建 md5 加密对象
              const md5 = crypto.createHash('md5')
              // 创建临时对象存储数据
              const temp = {}
              temp.href = href
              temp.id = md5.update(href).digest('hex')
              temp.title = a.text()
              // 数据添加到 news 相关的标题中
              specialTitles.push(temp)
            }

            // 把数据存进 redis,有效期为 1 天
            redis.set('specialTitles', specialTitles, 86400)

            resolve(specialTitles)
          })
          .catch(err => {
            console.log(err)
            reject(err)
          })
      }
    })
  })
}

// 获取专题列表
function getSpecialList(url) {
  // 新闻列表
  const specialList = []
  return new Promise((resolve, reject) => {
    // 判断 redis 中是否有数据,有则直接使用
    redis.get(url, result => {
      if (result) {
        resolve(result)
      } else {
        request(baseUrl + 'ztbd/' + url)
          .then(res => {
            // 解析文档
            const $ = cheerio.load(res)
            // 获取文章
            const news = $('ul.indexnewsul4 li')
            // 把数据添加到 newsList 中
            for (let i = 0; i < 4; i += 1) {
              // 获取新闻链接
              const a = $(news[i]).find('a')
              let href = a.attr('href')
              if (href.indexOf('http') > -1) {
                href = href.replace('https://news.zhku.edu.cn/', '')
              } else {
                href = href.replace(/\.\.\//g, '')
              }
              // 新建 md5 加密对象
              const md5 = crypto.createHash('md5')
              // 创建临时对象存储数据
              const temp = {}
              temp.href = href
              temp.id = md5.update(href).digest('hex')
              temp.title = a.text()
              temp.date = $(news[i]).find('span').text().trim()
              // 数据添加到 news 相关的标题中
              specialList.push(temp)
            }

            // 把数据存进 redis,有效期为 1 天
            redis.set(url, specialList, 86400)

            resolve(specialList)
          })
          .catch(err => {
            console.log(err)
            reject(err)
          })
      }
    })
  })
}

module.exports = {
  getSpecialTitle,
  getSpecialList,
}
