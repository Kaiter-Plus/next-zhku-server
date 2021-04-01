const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 新闻路径
const baseUrl = 'https://news.zhku.edu.cn/'

// 导出一个获取结果的一部函数
module.exports = url => {
  // 新闻列表
  const newsList = []
  return new Promise((resolve, reject) => {
    request(baseUrl + url)
      .then(res => {
        // 解析文档
        const $ = cheerio.load(res)
        // 获取文章
        const news = $('ul.indexnewsul4 li')
        // 把数据添加到 newsList 中
        for (let i = 0; i < news.length; i += 1) {
          // 获取新闻链接
          const a = $(news[i]).find('a')
          // 新建 md5 加密对象
          const md5 = crypto.createHash('md5')
          // 排除外网链接
          if (/http.*:\/\//g.test(a.attr('href'))) {
            continue
          }
          // 创建临时对象存储数据
          const temp = {}
          temp.href = a.attr('href')
          temp.id = md5.update(temp.href).digest('hex')
          temp.title = a.text()
          temp.date = $(news[i]).find('span').text().trim()
          // 数据添加到 news 相关的标题中
          newsList.push(temp)
        }
        resolve(newsList)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}
