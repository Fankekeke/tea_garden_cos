<template>
  <a-modal v-model="show" title="修改茶叶品种" @cancel="onClose" :width="750">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
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
      <a-modal
        :visible="growthRecordVisible"
        title="添加生长记录"
        @ok="handleGrowthRecordOk"
        @cancel="handleGrowthRecordCancel"
        okText="确定"
        cancelText="取消"
      >
        <a-form-model layout="vertical">
          <a-form-model-item label="阶段">
            <a-select
              v-model="tempStage"
              placeholder="请选择阶段"
            >
              <a-select-option value="施肥">施肥</a-select-option>
              <a-select-option value="除草">除草</a-select-option>
              <a-select-option value="采摘">采摘</a-select-option>
              <a-select-option value="修剪">修剪</a-select-option>
              <a-select-option value="灌溉">灌溉</a-select-option>
              <a-select-option value="病虫害防治">病虫害防治</a-select-option>
              <a-select-option value="发芽期">发芽期</a-select-option>
              <a-select-option value="生长期">生长期</a-select-option>
              <a-select-option value="成熟期">成熟期</a-select-option>
            </a-select>
          </a-form-model-item>

          <a-form-model-item label="操作详情/生长状态描述">
            <a-textarea
              v-model="tempContent"
              placeholder="请输入详细描述"
              :rows="3"
            />
          </a-form-model-item>

          <a-form-model-item label="记录日期">
            <a-date-picker
              v-model="tempRecordDate"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </a-form-model-item>
        </a-form-model>
      </a-modal>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
import moment from "moment";
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
  name: 'BulletinEdit',
  props: {
    productsEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.productsEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      growthRecords: [],
      growthRecordVisible: false,
      tempStage: '',
      tempContent: '',
      tempRecordDate: '',
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    queryRecordByProductId(productId) {
      this.$get('/cos/tea-records/queryRecordByProductId', {
        productId
      }).then((r) => {
        let data = r.data.data
        this.growthRecords = data
      })
    },
    setFormValues ({...bulletin}) {
      this.rowId = bulletin.id
      this.queryRecordByProductId(bulletin.id)
      let fields = ['name', 'variety', 'description']
      let obj = {}
      Object.keys(bulletin).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(bulletin['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = bulletin[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    reset () {
      this.loading = false
      this.form.resetFields()
      this.fileList = []
      this.growthRecords = []
    },
    addGrowthRecord() {
      this.growthRecordVisible = true
      // 重置临时数据
      this.tempStage = ''
      this.tempContent = ''
      this.tempRecordDate = ''
    },
    handleGrowthRecordOk() {
      if (!this.tempStage || !this.tempContent || !this.tempRecordDate) {
        this.$message.error('请填写完整信息')
        return
      }

      this.growthRecords.push({
        stage: this.tempStage,
        content: this.tempContent,
        recordDate: moment(this.tempRecordDate).format('YYYY-MM-DD')
      })

      this.$message.success('添加成功')
      this.growthRecordVisible = false
    },

    handleGrowthRecordCancel() {
      this.growthRecordVisible = false
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
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        values.record = this.growthRecords.length > 0 ? JSON.stringify(this.growthRecords) : null
        if (!err) {
          this.loading = true
          this.$put('/cos/tea-products', {
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
