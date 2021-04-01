const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 获取访问量
function getVisits() {
  // 新闻列表
  const visits = {}
  return new Promise((resolve, reject) => {
    request('https://www.zhku.edu.cn/')
      .then(res => {
        const $ = cheerio.load(res)
        const link = $('div.ft_info img')
        for (let i = 0; i < link.length; i += 1) {
          if (0 === i) {
            visits.today = 'https://www.zhku.edu.cn' + $(link[i]).attr('src')
          } else {
            visits.total = 'https://www.zhku.edu.cn' + $(link[i]).attr('src')
          }
        }
        resolve(visits)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

module.exports = {
  getVisits,
}
