import config from '@/config'

const title = process.env.VUE_APP_TITLE
const env = process.env.NODE_ENV
const ossUrl = getOssUrl()

function getOssUrl () {
  switch (env) {
    case 'development': {
      if (title === 'test') {
        return config.baseUrl.test
      } else if (title === 'mock') {
        return config.ossUrl.mock
      } else {
        return config.ossUrl.dev
      }
    }
    case 'production': {
      if (title === 'test') {
        return config.ossUrl.test
      } else if (title === 'mock') {
        return config.ossUrl.mock
      } else {
        return config.ossUrl.pro
      }
    }
  }
}

export { ossUrl }
