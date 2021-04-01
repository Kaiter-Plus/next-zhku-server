const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 获取友情链接
function getFriendLink() {
  // 链接列表
  const friendLink = []
  return new Promise((resolve, reject) => {
    request('https://www.zhku.edu.cn/')
      .then(res => {
        // 解析文档
        const $ = cheerio.load(res)
        // 获取专题标题
        const link = $('select')
        // 遍历添加数据
        for (let i = 0; i < link.length; i += 1) {
          const optiions = $(link[i]).find('option')
          for (let j = 0; j < $(optiions).length; j += 1) {
            // 新建 md5 加密对象
            const md5 = crypto.createHash('md5')
            // 创建临时对象存储数据
            const temp = {}
            if (j === 0) {
              temp.id = md5.update($(optiions[j]).text()).digest('hex')
              temp.title = $(optiions[j]).text()
              temp.children = []
              friendLink.push(temp)
            } else {
              temp.href = $(optiions[j]).attr('value')
              temp.id = md5.update(temp.href + j).digest('hex')
              temp.title = $(optiions[j]).text()
              friendLink[i].children.push(temp)
            }
          }
        }
        resolve(friendLink)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

module.exports = {
  getFriendLink,
}
