var express = require('express')
var router = express.Router()

// 导入请求
const schoolProfile = require('../../network/home/schoolProfile.js')

// 获取学校简介
router.get('/school-introduce', function (req, res, next) {
  schoolProfile
    .getSchoolIntroduce()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

// 获取现任领导
router.get('/incumbent', function (req, res, next) {
  schoolProfile
    .getIncumbent()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

// 获取校园风光
router.get('/school-scenery', function (req, res, next) {
  schoolProfile
    .getSchoolScenery()
    .then(value => {
      res.json(value)
    })
    .catch(reason => {
      res.write(reason)
    })
})

module.exports = router
