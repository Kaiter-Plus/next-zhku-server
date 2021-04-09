const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')
const redis = require('../../redis/index.js')

// 新闻网初始路径路径
const baseUrl = 'https://www.zhku.edu.cn/'

// 获取学校介绍
function getSchoolIntroduce() {
  return new Promise((resolve, reject) => {
    // 判断 redis 中是否有数据,有则直接使用
    redis.get('schoolIntroduce', result => {
      if (result) {
        resolve(result)
      } else {
        // 存储容器
        const schoolIntroduce = {}
        request(baseUrl + 'xxgk/xxjs.htm')
          .then(res => {
            const $ = cheerio.load(res)
            const container = $('div.schoolabout2')
            const title = container.find('h2').text()
            // 把标题添加进去
            schoolIntroduce.title = title
            // 初始化段落存储容器
            schoolIntroduce.content = []
            const paragraphs = container.find('p')
            for (let i = 0; i < paragraphs.length; i += 1) {
              const md5 = crypto.createHash('md5')
              const temp = {}
              temp.id = md5.update(String(i)).digest('hex')
              temp.text = $(paragraphs[i]).text()
              schoolIntroduce.content.push(temp)
            }

            // 把数据存进 redis,有效期为 1 天
            redis.set('schoolIntroduce', schoolIntroduce, 86400)

            resolve(schoolIntroduce)
          })
          .catch(err => {
            console.log(err)
            reject(err)
          })
      }
    })
  })
}

// 获取现任领导
function getIncumbent() {
  // 判断 redis 中是否有数据,有则直接使用
  let redis_incumbent = null
  redis.get('incumbent', result => {
    redis_incumbent = result
  })
  if (redis_incumbent) {
    resolve(redis_incumbent)
  }
  // 存储容器
  const incumbent = []
  return new Promise((resolve, reject) => {
    request(baseUrl + 'xxgk/xrld.htm')
      .then(res => {
        const $ = cheerio.load(res)
        const container = $('ul.picture_lingdao li')
        for (let i = 0; i < container.length; i += 1) {
          const a = $(container[i]).find('a')
          const md5 = crypto.createHash('md5')
          const temp = {}
          temp.href = a.attr('href')
          temp.title = $(container[i]).find('span').text().trim()
          temp.id = md5.update(temp.href).digest('hex')
          temp.src = baseUrl + a.find('img').attr('src').slice(1)
          incumbent.push(temp)
        }

        // 把数据存进 redis,有效期为 1 天
        redis.set('incumbent', incumbent, 86400)

        resolve(incumbent)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

// 获取校园风光
function getSchoolScenery() {
  // 判断 redis 中是否有数据,有则直接使用
  let redis_schoolScenery = null
  redis.get('schoolScenery', result => {
    redis_schoolScenery = result
  })
  if (redis_schoolScenery) {
    resolve(redis_schoolScenery)
  }
  // 存储容器
  const schoolScenery = []
  return new Promise((resolve, reject) => {
    request(baseUrl + 'xxgk/xyfg.htm')
      .then(res => {
        const $ = cheerio.load(res)
        const container = $('ul.picture_ul li')
        for (let i = 0; i < container.length; i += 1) {
          const a = $(container[i]).find('a')
          const md5 = crypto.createHash('md5')
          const temp = {}
          temp.title = $(container[i]).find('span').text().trim()
          temp.id = md5.update(temp.title).digest('hex')
          temp.src = baseUrl + a.find('img').attr('src').slice(1)
          schoolScenery.push(temp)
        }

        // 把数据存进 redis,有效期为 1 天
        redis.set('schoolScenery', schoolScenery, 86400)

        resolve(schoolScenery)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

module.exports = {
  getSchoolIntroduce,
  getIncumbent,
  getSchoolScenery,
}
