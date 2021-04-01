const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

// 新闻网初始路径路径
const baseUrl = 'https://www.zhku.edu.cn/'

// 获取学校介绍
function getSchoolIntroduce() {
  // 存储容器
  const schoolIntroduce = {}
  return new Promise((resolve, reject) => {
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
        resolve(schoolIntroduce)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

// 获取现任领导
function getIncumbent() {
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
