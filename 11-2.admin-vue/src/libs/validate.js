// 验证手机号格式
export const valideMoblie = (rule, value, callback) => {
  var reg = 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/
  if (value === '') {
    callback(new Error('请输入手机号码'))
  } else if (!reg.test(value)) {
    callback(new Error('手机格式不正确'))
  } else {
    callback()
  }
}
