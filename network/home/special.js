const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 新闻网初始路径路径
const baseUrl = 'https://news.zhku.edu.cn/'

// 获取专题标题
function getSpecialTitle(url) {
  // 新闻列表
  const specialTitles = []
  return new Promise((resolve, reject) => {
    request(baseUrl + url)
      .then(res => {
        // 解析文档
        const $ = cheerio.load(res)
        // 获取专题标题
        const titles = $('li.snav-li')
        // 遍历添加数据
        for (let i = 0; i < 4; i += 1) {
          const a = $(titles[i]).find('a')
          // 新建 md5 加密对象
          const md5 = crypto.createHash('md5')
          // 创建临时对象存储数据
          const temp = {}
          temp.href = a.attr('href')
          temp.id = md5.update(temp.href).digest('hex')
          temp.title = a.text()
          // 数据添加到 news 相关的标题中
          specialTitles.push(temp)
        }
        resolve(specialTitles)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

// 获取专题列表
function getSpecialList(url) {
  // 新闻列表
  const specialList = []
  return new Promise((resolve, reject) => {
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
          // 新建 md5 加密对象
          const md5 = crypto.createHash('md5')
          // 创建临时对象存储数据
          const temp = {}
          temp.href = a.attr('href')
          temp.id = md5.update(temp.href).digest('hex')
          temp.title = a.text()
          temp.date = $(news[i]).find('span').text().trim()
          // 数据添加到 news 相关的标题中
          specialList.push(temp)
        }
        resolve(specialList)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

module.exports = {
  getSpecialTitle,
  getSpecialList,
}
