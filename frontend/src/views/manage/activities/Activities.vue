<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="活动标题"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.title"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="发布人"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.publisher"/>
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
               :scroll="{ x: 900 }"
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
        <template slot="operation" slot-scope="text, record">
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
          <template slot="eventTime" slot-scope="text, record">
            <span>{{ text ? moment(text).format('YYYY-MM-DD HH:mm') : '--' }}</span>
          </template>
          <template slot="finishTime" slot-scope="text, record">
            <span>{{ text ? moment(text).format('YYYY-MM-DD HH:mm') : '--' }}</span>
          </template>
          <template slot="publisher" slot-scope="text, record">
            <span>{{ text || '--' }}</span>
          </template>
          <template slot="createdAt" slot-scope="text, record">
            <span>{{ text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : '--' }}</span>
          </template>
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
          <a-icon type="team" theme="twoTone" twoToneColor="#52c41a" @click="viewParticipants(record)" title="查看报名" style="margin-left: 10px;"></a-icon>
        </template>
      </a-table>
    </div>
    <a-drawer
      title="报名用户列表"
      :visible="participantsVisible"
      :width="700"
      @close="handleParticipantsClose"
      placement="right"
    >
      <a-table
        :columns="participantsColumns"
        :dataSource="participantsData"
        :pagination="false"
        :loading="participantsLoading"
        :scroll="{ y: 500 }"
      >
        <template slot="sexShow" slot-scope="text">
          <a-tag :color="text === 1 ? 'blue' : 'pink'">
            {{ text === 1 ? '男' : '女' }}
          </a-tag>
        </template>
        <template slot="joinTimeShow" slot-scope="text">
          <span>{{ text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : '--' }}</span>
        </template>
        <template slot="imagesShow" slot-scope="text">
          <a-avatar
            v-if="text"
            :src="'http://127.0.0.1:9527/imagesWeb/' + text"
            :size="40"
          />
          <a-avatar v-else icon="user" :size="40" />
        </template>
      </a-table>
    </a-drawer>
    <bulletin-add
      v-if="bulletinAdd.visiable"
      @close="handleBulletinAddClose"
      @success="handleBulletinAddSuccess"
      :bulletinAddVisiable="bulletinAdd.visiable">
    </bulletin-add>
    <bulletin-edit
      ref="bulletinEdit"
      @close="handleBulletinEditClose"
      @success="handleBulletinEditSuccess"
      :bulletinEditVisiable="bulletinEdit.visiable">
    </bulletin-edit>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import BulletinAdd from './ActivitiesAdd.vue'
import BulletinEdit from './ActivitiesEdit.vue'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Bulletin',
  components: {BulletinAdd, BulletinEdit, RangeDate},
  data () {
    return {
      advanced: false,
      bulletinAdd: {
        visiable: false
      },
      bulletinEdit: {
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
      participantsVisible: false,
      participantsData: [],
      participantsLoading: false,
      currentActivityId: null
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '互动标题',
        dataIndex: 'title',
        scopedSlots: { customRender: 'titleShow' },
        width: 200
      }, {
        title: '茶园活动内容',
        dataIndex: 'content',
        scopedSlots: { customRender: 'contentShow' },
        width: 300
      }, {
        title: '活动时间',
        dataIndex: 'eventTime',
        scopedSlots: { customRender: 'eventTime' },
        width: 200
      }, {
        title: '结束时间',
        dataIndex: 'finishTime',
        scopedSlots: { customRender: 'finishTime' },
        width: 200
      }, {
        title: '发布人',
        dataIndex: 'publisher',
        scopedSlots: { customRender: 'publisher' },
        width: 120
      }, {
        title: '创建时间',
        dataIndex: 'createdAt',
        scopedSlots: { customRender: 'createdAt' },
        width: 200
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'},
        width: 80
      }]
    },
    participantsColumns () {
      return [{
        title: '头像',
        dataIndex: 'images',
        scopedSlots: { customRender: 'imagesShow' },
        width: 80
      }, {
        title: '姓名',
        dataIndex: 'name',
        width: 150
      }, {
        title: '性别',
        dataIndex: 'sex',
        scopedSlots: { customRender: 'sexShow' },
        width: 80
      }, {
        title: '报名时间',
        dataIndex: 'joinTime',
        scopedSlots: { customRender: 'joinTimeShow' },
        width: 180
      }, {
        title: '编号',
        dataIndex: 'code',
        width: 150
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    viewParticipants (record) {
      this.currentActivityId = record.id
      this.participantsVisible = true
      this.queryParticipantsDetailById(record.id)
    },

    loadParticipantsList (activityId) {
      this.participantsLoading = true
      this.$get('/cos/activity-participants/list', {
        activityId: activityId
      }).then((r) => {
        console.log(r)
        if (r.data && r.data.list) {
          this.participantsData = r.data.list
        } else if (Array.isArray(r.data)) {
          this.participantsData = r.data
        } else {
          this.participantsData = []
        }
        this.participantsLoading = false
      }).catch(() => {
        this.participantsLoading = false
      })
    },

    handleParticipantsClose () {
      this.participantsVisible = false
      this.participantsData = []
      this.currentActivityId = null
    },

    queryParticipantsDetailById (id) {
      this.participantsLoading = true
      this.$get('/cos/activity-participants/queryDetailById', {
        id
      }).then((r) => {
        this.participantsData = r.data.data
        this.participantsLoading = false
      }).catch(() => {
        this.participantsLoading = false
      })
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.bulletinAdd.visiable = true
    },
    handleBulletinAddClose () {
      this.bulletinAdd.visiable = false
    },
    handleBulletinAddSuccess () {
      this.bulletinAdd.visiable = false
      this.$message.success('新增茶园活动成功')
      this.search()
    },
    edit (record) {
      this.$refs.bulletinEdit.setFormValues(record)
      this.bulletinEdit.visiable = true
    },
    handleBulletinEditClose () {
      this.bulletinEdit.visiable = false
    },
    handleBulletinEditSuccess () {
      this.bulletinEdit.visiable = false
      this.$message.success('修改茶园活动成功')
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
          that.$delete('/cos/activities/' + ids).then(() => {
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
      this.$get('/cos/activities/page', {
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
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
