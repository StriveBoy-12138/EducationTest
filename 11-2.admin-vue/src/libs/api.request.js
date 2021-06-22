import HttpRequest from '@/libs/axios'
import config from '@/config'

const title = process.env.VUE_APP_TITLE
const env = process.env.NODE_ENV
const baseUrl = getBaseUrl()

function getBaseUrl () {
  switch (env) {
    case 'development': {
      if (title === 'test') {
        return config.baseUrl.test
      } else if (title === 'mock') {
        return config.baseUrl.mock
      } else {
        return config.baseUrl.dev
      }
    }
    case 'production': {
      if (title === 'test') {
        return config.baseUrl.test
      } else if (title === 'mock') {
        return config.baseUrl.mock
      } else {
        return config.baseUrl.pro
      }
    }
  }
}

const axios = new HttpRequest(baseUrl)
export default axios
