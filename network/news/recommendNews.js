const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')
const redis = require('../../redis/index.js')

// 导出一个获取结果的一部函数
module.exports = () => {
  // 新闻列表
  const recommendNews = []
  return new Promise((resolve, reject) => {
    // 判断 redis 中是否有数据,有则直接使用
    redis.get('recommendNews', result => {
      if (result) {
        resolve(result)
      } else {
        request({
          url: 'https://news.zhku.edu.cn/',
          headers: {
            Host: 'news.zhku.edu.cn',
            Referer: 'https://news.zhku.edu.cn/',
          },
        })
          .then(res => {
            const $ = cheerio.load(res)
            const news = $('.top5_l a')
            for (let i = 0; i < news.length; i += 1) {
              const img = $(news[i]).find('img')
              // 新建 md5 加密对象
              const md5 = crypto.createHash('md5')
              const temp = {}
              temp.href = $(news[i]).attr('href').replace('http://news.zhku.edu.cn/', '')
              // temp.href = 'https://news.zhku.edu.cn/' + temp.href
              temp.id = md5.update(temp.href).digest('hex')
              temp.title = $(news[i]).attr('title')
              temp.imgSrc = 'https://news.zhku.edu.cn' + img.attr('src')
              // 数据添加到 news 相关的标题中
              recommendNews.push(temp)
            }

            // 把数据存进 redis,有效期为 1 天
            redis.set('recommendNews', recommendNews, 86400)

            resolve(recommendNews)
          })
          .catch(err => {
            console.log(err)
            reject(err)
          })
      }
    })
  })
}
