const request = require('../index.js')
const cheerio = require('cheerio')
const crypto = require('crypto')
const redis = require('../../redis/index.js')

const url = 'https://www.zhku.edu.cn/'
const home = {}

module.exports = () => {
  return new Promise((resolve, reject) => {
    // 判断 redis 中是否有数据,有则直接使用
    redis.get('home', result => {
      if (result) {
        resolve(result)
      } else {
        request({
          url,
          headers: {
            Host: 'www.zhku.edu.cn',
            Referer: 'https://www.zhku.edu.cn/',
          },
        })
          .then(res => {
            const $ = cheerio.load(res)

            // 获取 Banner 地址
            getBanner($)
            // 获取轮播图地址
            getImages($)
            // 获取首页新闻标题
            getNewsTitles($)
            // 获取友情链接
            getFriendLink($)
            // 获取网站访问量
            getVisits($)

            // 把数据存进 redis,有效期为 1 天
            redis.set('home', home, 86400)

            // 返回结果
            resolve(home)
          })
          .catch(err => {
            console.log(err)
            reject(err)
          })
      }
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

// 虎丘新闻标题和地址
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

// 获取友情链接
function getFriendLink($) {
  // 链接列表
  home.friendLink = []
  // 获取所有的友情链接容器
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
        // 标题
        temp.id = md5.update($(optiions[j]).text()).digest('hex')
        temp.title = $(optiions[j]).text()
        temp.children = []
        home.friendLink.push(temp)
      } else {
        // 友情链接项
        temp.href = $(optiions[j]).attr('value')
        temp.id = md5.update(temp.href + j).digest('hex')
        temp.title = $(optiions[j]).text()
        home.friendLink[i].children.push(temp)
      }
    }
  }
}

// 获取访问量
function getVisits($) {
  // 访问量图片地址
  home.visits = {}
  const link = $('div.ft_info img')
  for (let i = 0; i < link.length; i += 1) {
    if (0 === i) {
      home.visits.today = 'https://www.zhku.edu.cn' + $(link[i]).attr('src')
    } else {
      home.visits.total = 'https://www.zhku.edu.cn' + $(link[i]).attr('src')
    }
  }
}
