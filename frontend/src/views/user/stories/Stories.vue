
<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="故事标题"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.title"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="状态"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.status" allowClear>
                  <a-select-option value="待审核">待审核</a-select-option>
                  <a-select-option value="通过">通过</a-select-option>
                  <a-select-option value="驳回">驳回</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="茶农姓名"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button type="primary" ghost @click="add">新增</a-button>
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 1200 }"
               @change="handleTableChange">
        <template slot="titleShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.title }}
              </template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.content }}
              </template>
              {{ record.content.slice(0, 30) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="userNameShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.name }}
              </template>
              {{ record.name }}
            </a-tooltip>
          </template>
        </template>
        <template slot="statusShow" slot-scope="text, record">
          <a-tag :color="record.status === '待审核' ? 'orange' : record.status === '通过' ? 'green' : 'red'">
            {{ record.status }}
          </a-tag>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
        </template>
      </a-table>
    </div>
    <stories-add
      v-if="storiesAdd.visiable"
      @close="handleStoriesAddClose"
      @success="handleStoriesAddSuccess"
      :storiesAddVisiable="storiesAdd.visiable">
    </stories-add>
    <stories-edit
      ref="storiesEdit"
      @close="handleStoriesEditClose"
      @success="handleStoriesEditSuccess"
      :storiesEditVisiable="storiesEdit.visiable">
    </stories-edit>
  </a-card>
</template>

<script>import RangeDate from '@/components/datetime/RangeDate'
import StoriesAdd from './StoriesAdd.vue'
import StoriesEdit from './StoriesEdit.vue'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Stories',
  components: {StoriesAdd, StoriesEdit, RangeDate},
  data () {
    return {
      advanced: false,
      storiesAdd: {
        visiable: false
      },
      storiesEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: [],
      auditVisible: false,
      auditLoading: false,
      currentRecord: {},
      auditForm: {
        id: null,
        status: '1',
        auditRemark: ''
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '故事标题',
        dataIndex: 'title',
        scopedSlots: { customRender: 'titleShow' },
        width: 200
      }, {
        title: '故事内容',
        dataIndex: 'content',
        scopedSlots: { customRender: 'contentShow' },
        width: 300
      }, {
        title: '状态',
        dataIndex: 'status',
        scopedSlots: { customRender: 'statusShow' },
        width: 100
      }, {
        title: '审核备注',
        dataIndex: 'auditRemark',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        width: 150
      }, {
        title: '茶农姓名',
        dataIndex: 'name',
        scopedSlots: { customRender: 'userNameShow' },
        width: 120
      }, {
        title: '茶农编号',
        dataIndex: 'code',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        width: 120
      }, {
        title: '性别',
        dataIndex: 'sex',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text == '0' ? '男' : '女'
          } else {
            return '- -'
          }
        },
        width: 80
      }, {
        title: '创建时间',
        dataIndex: 'createdAt',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        width: 150
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'},
        width: 80
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    audit (record) {
      this.currentRecord = record
      this.auditForm = {
        id: record.id,
        status: record.status || '1',
        auditRemark: record.auditRemark || ''
      }
      this.auditVisible = true

      // 重置表单验证
      if (this.$refs.auditForm) {
        this.$refs.auditForm.clearValidate()
      }
    },

    handleAuditSubmit () {
      this.$refs.auditForm.validate(valid => {
        if (valid) {
          this.auditLoading = true
          this.$put('/cos/stories', {
            ...this.auditForm
          }).then(() => {
            this.$message.success('审核成功')
            this.auditVisible = false
            this.search()
          }).catch(() => {
            this.$message.error('审核失败，请重试')
          }).finally(() => {
            this.auditLoading = false
          })
        }
      })
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.storiesAdd.visiable = true
    },
    handleStoriesAddClose () {
      this.storiesAdd.visiable = false
    },
    handleStoriesAddSuccess () {
      this.storiesAdd.visiable = false
      this.$message.success('新增茶农故事成功')
      this.search()
    },
    edit (record) {
      this.$refs.storiesEdit.setFormValues(record)
      this.storiesEdit.visiable = true
    },
    handleStoriesEditClose () {
      this.storiesEdit.visiable = false
    },
    handleStoriesEditSuccess () {
      this.storiesEdit.visiable = false
      this.$message.success('修改茶农故事成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/stories/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      params.userId = this.currentUser.userId
      this.$get('/cos/stories/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>@import "../../../../static/less/Common";
</style>
