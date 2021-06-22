<template>
  <div>
    <Row>
      <Col span="12">
        <h2>编辑（图片上传使用阿里OSS）</h2>
        <tinymce-editor id='characterTinymce22' :content.sync='content'></tinymce-editor>
      </Col>
      <Col span="12">
        <div style="margin-left: 10px;">
            <h2>预览</h2>
            <div v-html="content"></div>
        </div>
      </Col>
    </Row>

  </div>
</template>
<script>
import tinymceEditor from '_c/text-editor/tinymce-editor.vue'
export default {
  name: 'demo',
  components: {
    tinymceEditor
  },
  data () {
    return {
      content: '富文本初始值。。。'
    }
  },
  methods: {}
}
</script>
