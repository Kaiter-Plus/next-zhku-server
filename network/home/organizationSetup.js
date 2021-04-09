const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')
const redis = require('../../redis/index.js')

// 网址前缀
const baseUrl = 'https://www.zhku.edu.cn/jgsz/'

// 获取机构设置
function getOrganizationContent(url) {
  return new Promise((resolve, reject) => {
    // 判断 redis 中是否有数据,有则直接使用
    redis.get(url, result => {
      if (result) {
        resolve(result)
      } else {
        // 结构设置链接
        const orgLink = []
        request(baseUrl + url)
          .then(res => {
            // 解析文档
            const $ = cheerio.load(res)
            // 获取所有链接
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

            // 把数据存进 redis,有效期为 1 天
            redis.set(url, orgLink, 86400)

            resolve(orgLink)
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
  getOrganizationContent,
}
