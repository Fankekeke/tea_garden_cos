
<template>
  <a-modal v-model="show" title="新增茶园活动" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='活动主题' v-bind="formItemLayout">
            <a-input v-decorator="[
            'title',
            { rules: [{ required: true, message: '请输入活动主题!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='发布人' v-bind="formItemLayout">
            <a-input v-decorator="[
            'publisher',
            { rules: [{ required: true, message: '请输入发布人!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='活动举办时间' v-bind="formItemLayout">
            <a-date-picker
              v-decorator="[
                'eventTime',
                { rules: [{ required: true, message: '请选择活动举办时间!' }] }
              ]"
              show-time
              format="YYYY-MM-DD HH:mm"
              placeholder="请选择活动举办时间"              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='活动结束时间' v-bind="formItemLayout">
            <a-date-picker
              v-decorator="[
                'finishTime',
                { rules: [{ required: true, message: '请选择活动结束时间!' }] }
              ]"
              show-time
              format="YYYY-MM-DD HH:mm"
              placeholder="请选择活动结束时间"              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='活动详情' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入活动详情!' }] }
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>import {mapState} from 'vuex'
import moment from 'moment'

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}

export default {
  name: 'ActivitiesAdd',
  props: {
    bulletinAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    moment,
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          // 处理日期格式
          if (values.eventTime) {
            values.eventTime = moment(values.eventTime).format('YYYY-MM-DD HH:mm:ss')
          }
          if (values.finishTime) {
            values.finishTime = moment(values.finishTime).format('YYYY-MM-DD HH:mm:ss')
          }

          this.loading = true
          this.$post('/cos/activities', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped></style>
