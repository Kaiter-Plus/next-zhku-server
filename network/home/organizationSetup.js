const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 网址前缀
const baseUrl = 'https://www.zhku.edu.cn/jgsz/'

// 获取友情链接
function getOrganizationContent(url) {
  // 新闻列表
  const orgLink = []
  return new Promise((resolve, reject) => {
    request(baseUrl + url)
      .then(res => {
        // 解析文档
        const $ = cheerio.load(res)
        // 获取专题标题
        const link = $('div.schoolabout p')
        // 遍历添加数据
        for (let i = 0; i < link.length; i += 1) {
          const a = $(link[i]).find('a')
          // 新建 md5 加密对象
          const md5 = crypto.createHash('md5')
          // 创建临时对象存储数据
          const temp = {}
          temp.href = a.attr('href')
          temp.id = md5.update(temp.href).digest('hex')
          temp.title = a.text()
          orgLink.push(temp)
        }
        resolve(orgLink)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

module.exports = {
  getOrganizationContent,
}
