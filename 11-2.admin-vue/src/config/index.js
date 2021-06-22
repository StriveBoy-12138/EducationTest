export default {
  /**
   * @description 项目名称
   */
  projectName: '学习考试系统',

  /**
   * @description 配置显示在浏览器标签的title
   */
  /* eslint-disable */
  title: function(){
    return this.projectName + '管理后台'
  },

  /**
   * @description 项目在一个域名下的唯一标识
   */
  projectId: 'examinationAdmin',

  /**
   * @description token在Cookie中存储的天数，默认1天
   */
  cookieExpires: 1,

  /**
   * @description 是否使用国际化，默认为false
   *              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
   *              用来在菜单中显示文字
   */
  useI18n: false,

  /**
   * @description api请求基础路径
   */
  baseUrl: {
    // 开发环境
    dev: 'http://localhost:8082/examination-admin-api/',
    // 生产环境
    pro: 'http://116.62.187.191/examination-admin-api/',
    // 测试环境
    test: 'https://test.cn1.utools.club/common-admin-api/',
    // mock模拟数据
    mock: 'http://worker.njbandou.com:8999/mock/137/'
  },

  ossUrl: {
    // 开发环境
    dev: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/',
    // 生产环境
    pro: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/',
    // 测试环境
    test: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/',
    // mock模拟数据
    mock: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/'
  },

  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home'
}
