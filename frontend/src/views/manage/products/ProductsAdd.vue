
<template>
  <a-modal v-model="show" title="新增茶叶产品" @cancel="onClose" :width="650">
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
          <a-form-item label='茶叶名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入茶叶名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='品种' v-bind="formItemLayout">
            <a-input v-decorator="[
            'variety',
            { rules: [{ required: true, message: '请输入品种!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='生长习性及详细描述' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'description',
             { rules: [{ required: true, message: '请输入详细描述!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='生长周期记录' v-bind="formItemLayout">
            <a-button type="primary" icon="plus" @click="addGrowthRecord" style="margin-bottom: 10px;">
              添加生长记录
            </a-button>
            <a-table
              :columns="growthColumns"
              :dataSource="growthRecords"
              :pagination="false"
              bordered
              size="small">
              <template slot="action" slot-scope="text, record, index">
                <a-popconfirm title="确定删除吗？" @confirm="deleteGrowthRecord(index)">
                  <a-button type="danger" size="small" icon="delete">删除</a-button>
                </a-popconfirm>
              </template>
            </a-table>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>import {mapState} from 'vuex'
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
  name: 'ProductsAdd',
  props: {
    productsAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.productsAddVisiable
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
      previewImage: '',
      growthRecords: [],
      growthColumns: [
        {
          title: '阶段',
          dataIndex: 'stage',
          key: 'stage',
          width: 150,
          scopedSlots: { customRender: 'stage' }
        },
        {
          title: '操作详情/生长状态',
          dataIndex: 'content',
          key: 'content',
          width: 300,
          scopedSlots: { customRender: 'content' }
        },
        {
          title: '记录日期',
          dataIndex: 'recordDate',
          key: 'recordDate',
          width: 150,
          scopedSlots: { customRender: 'recordDate' }
        },
        {
          title: '操作',
          key: 'action',
          width: 100,
          scopedSlots: { customRender: 'action' }
        }
      ]
    }
  },
  methods: {
    handleRemove(file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    beforeUpload(file) {
      this.fileList = [...this.fileList, file];
      return false;
    },
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
      this.fileList = []
      this.growthRecords = []
    },
    addGrowthRecord () {
      this.$modal.info({
        title: '添加生长记录',
        content: h => {
          return h('div', [
            h('a-form-model', { props: { layout: 'vertical' } }, [
              h('a-form-model-item', { props: { label: '阶段' } }, [
                h('a-select', {
                  props: { placeholder: '请选择阶段', value: this.tempStage },
                  on: { change: (val) => { this.tempStage = val } }
                }, [
                  h('a-select-option', { props: { value: '施肥' } }, '施肥'),
                  h('a-select-option', { props: { value: '除草' } }, '除草'),
                  h('a-select-option', { props: { value: '采摘' } }, '采摘'),
                  h('a-select-option', { props: { value: '修剪' } }, '修剪'),
                  h('a-select-option', { props: { value: '灌溉' } }, '灌溉'),
                  h('a-select-option', { props: { value: '病虫害防治' } }, '病虫害防治'),
                  h('a-select-option', { props: { value: '发芽期' } }, '发芽期'),
                  h('a-select-option', { props: { value: '生长期' } }, '生长期'),
                  h('a-select-option', { props: { value: '成熟期' } }, '成熟期')
                ])
              ]),
              h('a-form-model-item', { props: { label: '操作详情/生长状态描述' } }, [
                h('a-textarea', {
                  props: {
                    placeholder: '请输入详细描述',
                    rows: 3,
                    value: this.tempContent
                  },
                  on: { input: (e) => { this.tempContent = e.target.value } }
                })
              ]),
              h('a-form-model-item', { props: { label: '记录日期' } }, [
                h('a-date-picker', {
                  props: {
                    value: this.tempRecordDate ? moment(this.tempRecordDate) : null,
                    format: 'YYYY-MM-DD'
                  },
                  on: { change: (date, dateString) => { this.tempRecordDate = dateString } }
                })
              ])
            ])
          ])
        },
        onOk: () => {
          if (!this.tempStage || !this.tempContent || !this.tempRecordDate) {
            this.$message.error('请填写完整信息')
            return false
          }
          this.growthRecords.push({
            stage: this.tempStage,
            content: this.tempContent,
            recordDate: this.tempRecordDate
          })
          this.tempStage = ''
          this.tempContent = ''
          this.tempRecordDate = ''
          this.$message.success('添加成功')
        }
      })
    },
    deleteGrowthRecord (index) {
      this.growthRecords.splice(index, 1)
      this.$message.success('删除成功')
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片 List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        values.growthRecords = this.growthRecords.length > 0 ? this.growthRecords : null
        if (!err) {
          this.loading = true
          this.$post('/cos/tea-products', {
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

<style scoped>
</style>
