
<template>
  <div class="products-container">
    <a-card :bordered="false" class="products-card">
      <h2 class="page-title">茶叶产品</h2>

      <a-list
        :grid="{ gutter: 16, column: 1 }"
        :data-source="productsList"
        :loading="loading"
      >
        <a-list-item slot="renderItem" slot-scope="item">
          <a-card hoverable @click="viewDetail(item)">
            <a-row :gutter="16">
              <a-col :span="6">
                <img
                  v-if="item.images"
                  :src="'http://127.0.0.1:9527/imagesWeb/' + item.images"
                  :alt="item.name"
                  class="product-image"
                />
                <div v-else class="no-image">
                  <a-icon type="picture" theme="outlined" />
                </div>
              </a-col>
              <a-col :span="18">
                <a-card-meta>
                  <div slot="title" class="product-name">{{ item.name }}</div>
                  <div slot="description">
                    <div class="product-variety">
                      <a-tag color="green">品种：{{ item.variety }}</a-tag>
                    </div>
                    <div class="product-description">
                      {{ item.description }}
                    </div>
                    <div class="product-info">
                      <div class="info-row">
                        <a-icon type="clock-circle" />
                        <span class="label">创建时间：</span>
                        <span>{{ item.createdAt }}</span>
                      </div>
                    </div>
                  </div>
                </a-card-meta>
              </a-col>
            </a-row>

            <div class="record-section" v-if="item.teaRecords && item.teaRecords.length > 0">
              <div class="record-title">
                <a-icon type="file-text" />
                <span>种植采摘记录</span>
                <a-tag color="blue">{{ item.teaRecords.length }}条记录</a-tag>
              </div>

              <a-timeline>
                <a-timeline-item
                  v-for="record in item.teaRecords"
                  :key="record.id"
                  color="green"
                >
                  <template slot="dot">
                    <a-icon
                      type="check-circle"
                      style="font-size: 16px; color: #52c41a;"
                    />
                  </template>
                  <div class="record-item">
                    <div class="record-header">
                      <a-tag color="blue">{{ record.stage }}</a-tag>
                      <span class="record-date">{{ record.recordDate }}</span>
                    </div>
                    <div class="record-content">
                      {{ record.content }}
                    </div>
                    <div class="record-time">
                      <a-icon type="clock-circle" />
                      <span>{{ record.createdAt }}</span>
                    </div>
                  </div>
                </a-timeline-item>
              </a-timeline>
            </div>
            <div v-else class="no-record">
              <a-empty description="暂无种植采摘记录" />
            </div>
          </a-card>
        </a-list-item>
      </a-list>
    </a-card>

    <!-- 详情抽屉 -->
    <a-drawer
      title="产品详情"
      :visible="detailVisible"
      :width="700"
      @close="handleDetailClose"
      placement="right"
    >
      <div v-if="currentProduct">
        <a-descriptions bordered :column="1">
          <a-descriptions-item label="产品名称">
            {{ currentProduct.name }}
          </a-descriptions-item>
          <a-descriptions-item label="品种">
            <a-tag color="green">{{ currentProduct.variety }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="描述">
            {{ currentProduct.description }}
          </a-descriptions-item>
          <a-descriptions-item label="图片">
            <img
              v-if="currentProduct.images"
              :src="'http://127.0.0.1:9527/imagesWeb/' + currentProduct.images"
              :alt="currentProduct.name"
              class="detail-image"
            />
            <div v-else class="no-image">
              <a-icon type="picture" theme="outlined" />
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ currentProduct.createdAt }}
          </a-descriptions-item>
        </a-descriptions>

        <a-divider orientation="left">种植采摘记录</a-divider>

        <a-table
          v-if="currentProduct.teaRecords && currentProduct.teaRecords.length > 0"
          :columns="recordColumns"
          :dataSource="currentProduct.teaRecords"
          :pagination="false"
          :scroll="{ y: 400 }"
        >
          <template slot="stageShow" slot-scope="text">
            <a-tag color="blue">{{ text }}</a-tag>
          </template>
          <template slot="recordDateShow" slot-scope="text">
            <span>{{ text ? moment(text).format('YYYY-MM-DD') : '--' }}</span>
          </template>
          <template slot="createdAtShow" slot-scope="text">
            <span>{{ text ? moment(text).format('YYYY-MM-DD HH:mm:ss') : '--' }}</span>
          </template>
        </a-table>
        <a-empty v-else description="暂无记录" />
      </div>
    </a-drawer>
  </div>
</template>


<script>import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: "Products",
  data() {
    return {
      productsList: [],
      loading: false,
      detailVisible: false,
      currentProduct: null,
      recordColumns: [{
        title: '阶段',
        dataIndex: 'stage',
        scopedSlots: { customRender: 'stageShow' },
        width: 100
      }, {
        title: '内容',
        dataIndex: 'content',
        ellipsis: true,
        width: 300
      }, {
        title: '记录日期',
        dataIndex: 'recordDate',
        scopedSlots: { customRender: 'recordDateShow' },
        width: 120
      }, {
        title: '创建时间',
        dataIndex: 'createdAt',
        scopedSlots: { customRender: 'createdAtShow' },
        width: 160
      }]
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted() {
    this.queryActivitiesList()
  },
  methods: {
    queryActivitiesList() {
      this.loading = true
      this.$get('/cos/tea-products/list').then((r) => {
        console.log(r)
        this.productsList = r.data.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    viewDetail(item) {
      this.currentProduct = item
      this.detailVisible = true
    },

    handleDetailClose() {
      this.detailVisible = false
      this.currentProduct = null
    }
  }
}
</script>


<style scoped>.products-container {
  padding: 24px;
}

.products-card {
  background: #fff;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #333;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.no-image {
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 8px;
  font-size: 48px;
  color: #d9d9d9;
}

.product-name {
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
}

.product-variety {
  margin-bottom: 12px;
}

.product-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
}

.product-info {
  padding-top: 12px;
  border-top: 1px solid #e8e8e8;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

.info-row .anticon {
  margin-right: 8px;
  color: #1890ff;
}

.info-row .label {
  font-weight: 500;
  width: 80px;
}

.record-section {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #e8e8e8;
}

.record-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}

.record-item {
  padding: 12px;
  background: #f5f5f5;
  border-radius: 4px;
}

.record-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.record-date {
  font-size: 13px;
  color: #999;
}

.record-content {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 8px;
}

.record-time {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #999;
}

.record-time .anticon {
  margin-right: 4px;
}

.no-record {
  margin-top: 24px;
  text-align: center;
}

.detail-image {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
  border-radius: 8px;
}
</style>
