const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 新闻路径
const baseUrl = 'https://news.zhku.edu.cn/'

// 导出一个获取结果的一部函数
module.exports = params => {
  let url
  // 判断传入的参数时是不是一个对象
  typeof params === 'object' ? (url = `${params.type}/${params.url}`) : (url = params)
  // 新闻列表
  const newsList = {}
  newsList.news = []
  return new Promise((resolve, reject) => {
    request(baseUrl + url)
      .then(res => {
        // 解析文档
        const $ = cheerio.load(res)
        // 获取总分页数
        const totalPage = $('[id*="fanye"]').text().split(/\s+/g)[1].split('/')[1]
        newsList.totalPage = totalPage
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
          temp.href = a.attr('href').replace('../', '')
          temp.id = md5.update(temp.href + i).digest('hex')
          temp.title = a.text()
          temp.date = $(news[i]).find('span').text().trim()
          // 数据添加到 news 相关的标题中
          newsList.news.push(temp)
        }
        resolve(newsList)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}
