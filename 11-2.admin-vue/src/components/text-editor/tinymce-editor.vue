<template>
    <div>
      <!--
        给editor加key是因为给tinymce keep-alive以后组件切换时tinymce编辑器会显示异常，
        在activated钩子里改变key的值可以让编辑器重新创建
      -->
      <editor :id="id" :init="tinymceInit" v-model="textContent" :key="tinymceFlag"></editor>
    </div>
    </template>
<script>
// eslint-disable-next-line
import tinymce from 'tinymce/tinymce'
import 'tinymce/themes/silver/theme'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/plugins/textcolor'
import 'tinymce/plugins/advlist'
import 'tinymce/plugins/table'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/paste'
import 'tinymce/plugins/preview'
import 'tinymce/plugins/fullscreen'
import 'tinymce/plugins/image'
import 'tinymce/plugins/imagetools'
import 'tinymce/plugins/code'
import 'tinymce/plugins/emoticons'

import { oss } from '@/libs/ossUtil'
export default {
  name: 'TinymceEditor',
  components: {
    'editor': Editor
  },
  props: {
    showMenubar: Boolean,
    h: String,
    content: String,
    id: String
  },
  data () {
    return {
      publicPath: process.env.BASE_URL,
      tinymceFlag: 1,
      tinymceInit: {}
    }
  },
  methods: {
  },
  created () {
    let baseUrl = this.publicPath === '/' ? '' : this.publicPath
    this.tinymceInit = {
      themes: 'modern',
      skin_url: baseUrl + `/tinymce/skins/ui/oxide`,
      language_url: baseUrl + `/tinymce/langs/zh_CN.js`,
      language: 'zh_CN',
      height: typeof this.h === 'undefined' ? (document.body.offsetHeight - 300) : Number(this.h),
      browser_spellcheck: true, // 拼写检查
      branding: false, // 去水印
      statusbar: false, // 隐藏编辑器底部的状态栏
      paste_data_images: true, // 允许粘贴图像
      menubar: !this.showMenubar, // 最上方菜单
      image_advtab: true, // 图片高级样式
      images_upload_url: 'none', // 图片上传地址，此配置开启本地上传功能
      emoticons_database_url: baseUrl + `/tinymce/plugins/emoticons/js/emojis.min.js`, // emoji表情地址
      plugins: 'advlist table lists paste preview fullscreen image imagetools code emoticons',
      toolbar: 'fontselect fontsizeselect forecolor backcolor bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | quicklink h2 h3 blockquote table numlist bullist preview fullscreen | image  imagetools code emoticons',
      // 自定义上传图片
      images_upload_handler: function (blobInfo, success, failure) {
        var xhr, formData, host
        oss(blobInfo.filename(), 2).then(res => {
          formData = new FormData()
          Object.keys(res).forEach((key) => {
            formData.append(key, res[key])
          })
          host = res.host
          xhr = new XMLHttpRequest()
          xhr.withCredentials = false
          xhr.open('POST', host)
          xhr.onload = function () {
            var json
            if (xhr.status !== 200) {
              failure('HTTP Error: ' + xhr.status)
              return
            }
            json = JSON.parse(xhr.responseText)
            if (!json || typeof json.location !== 'string') {
              failure('Invalid JSON: ' + xhr.responseText)
              return
            }
            success(json.location)
          }
          formData.append('file', blobInfo.blob(), blobInfo.filename())
          xhr.send(formData)
        })
      }
    //   /**
    //        * 下面方法是为tinymce添加自定义插入图片按钮
    //        * 借助iview的Upload组件,将图片先上传到存储云上，再将图片的存储地址放入编辑器内容
    //        */
    //   setup: (editor) => {
    //     editor.ui.registry.addButton('imageUpload', {
    //       tooltip: '插入图片',
    //       icon: 'image',
    //       onAction: () => {
    //         let upload = that.$refs.imageUpload
    //         upload.handleClick()
    //       }
    //     })
    //   }
    }
  },
  activated () {
    this.tinymceFlag++
  },
  mounted () {
  },
  computed: {
    textContent: {
      get: function () {
        return this.content
      },
      set: function (newValue) {
        this.$emit('update:content', newValue)
      }
    }
  }
}
</script>
