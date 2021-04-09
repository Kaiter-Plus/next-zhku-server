var express = require('express')
var router = express.Router()

// 导入请求
const getNewsList = require('../../network/news/newsList.js')
const getrecommendNews = require('../../network/news/recommendNews.js')
const getNews = require('../../network/news/news.js')

// 获取推荐新闻
router.get('/', function (req, res, next) {
  getrecommendNews()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

// 获取新闻列表
router.get('/:url', function (req, res, next) {
  getNewsList(req.params.url)
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

// 获取新的新闻列表
router.get('/:type/:url', function (req, res, next) {
  getNewsList(req.params)
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

// 获取新闻列表
router.get('/:info/:type/:no', function (req, res, next) {
  getNews(req.params)
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.send(reason)
    })
})

module.exports = router
