var express = require('express')
var router = express.Router()

// 导入请求
const getNewsList = require('../../network/news/newsList.js')
const getrecommendNews = require('../../network/news/recommendNews.js')

// 获取新闻列表
router.get('/', function (req, res, next) {
  getrecommendNews()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

// 获取新闻列表
router.get('/:url', function (req, res, next) {
  getNewsList(req.params.url)
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

module.exports = router
