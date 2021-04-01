const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')

const url = 'https://www.zhku.edu.cn/'
const home = {}

module.exports = () => {
  return new Promise((resolve, reject) => {
    request(url)
      .then(res => {
        const $ = cheerio.load(res)
        getBanner($)
        getImages($)
        getNewsTitles($)
        resolve(home)
      })
      .catch(err => {
        console.log(err)
        reject(err)
      })
  })
}

// 获取 banner 图
function getBanner($) {
  // 存放图片数组
  const img = $('.banner_box img')
  home.bannerSrc = url + img.attr('src')
}

// 获取轮播图地址
function getImages($) {
  // 存放图片数组
  home.images = []
  const imgs = Array.from($('.bd img'))
  for (const index in imgs) {
    const src = $(imgs[index]).attr('src')
    const md5 = crypto.createHash('md5')
    const temp = {}
    temp.id = md5.update(src).digest('hex')
    temp.alt = src.split('/')[1]
    temp.src = url + src
    home.images.push(temp)
  }
}

function getNewsTitles($) {
  // 存放新闻列表
  home.newsTitles = []
  // 获取新闻列表标题以及链接地址
  const newsTitles = Array.from($('.js_ul li a'))
  for (const i in newsTitles) {
    const md5 = crypto.createHash('md5')
    // 把 标题 初始化为数组
    const title = $(newsTitles[i]).text().trim()
    const href = $(newsTitles[i]).attr('href')
    const newsItem = {}
    newsItem.title = title
    newsItem.id = md5.update(title).digest('hex')
    newsItem.href = href
    home.newsTitles.push(newsItem)
  }
}
