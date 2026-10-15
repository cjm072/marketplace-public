<template>
  <div class="analytic-data">
    <div class="date-filter">
      <el-select 
        v-model="store.state.analyticData.selectedYear" 
        placeholder="Select Year" 
        @change="$emit('date-change')"
      >
        <el-option
          v-for="year in store.state.analyticData.availableYears"
          :key="year"
          :label="year"
          :value="year"
        />
      </el-select>
      
      <el-select 
        v-model="store.state.analyticData.selectedMonth" 
        placeholder="Select Month" 
        @change="$emit('date-change')"
      >
        <el-option
          v-for="(month, index) in store.state.analyticData.months"
          :key="index"
          :label="month"
          :value="index + 1"
        />
      </el-select>
      
      <el-button type="primary" @click="$emit('date-change')">
        Apply Filter
      </el-button>
    </div>

    <div class="sales-cards">
      <el-card class="sales-card">
        <div class="card-title">Orders</div>
        <div class="card-value">{{ store.state.analyticData.salesData.orders.toLocaleString() }}</div>
      </el-card>
      <el-card class="sales-card">
        <div class="card-title">Sales</div>
        <div class="card-value">RM{{ store.state.analyticData.salesData.sales.toLocaleString() }}</div>
      </el-card>
      <el-card class="sales-card">
        <div class="card-title">Average</div>
        <div class="card-value">RM{{ store.state.analyticData.salesData.average.toFixed(2) }}</div>
      </el-card>
    </div>

    <div class="charts-container">
      <div class="chart-container">
        <h3>Sales Percentage ({{ store.selectedMonthName }} {{ store.state.analyticData.selectedYear }})</h3>
        <v-chart class="chart" :option="salesPieChart" autoresize />
      </div>
      
      <div class="chart-container">
        <h3>Percentage of Sales Increased vs Previous Month</h3>
        <v-chart class="chart" :option="productIncreaseChart" autoresize />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAllDataStore } from '@/stores';
import { use } from 'echarts/core';
import { BarChart, PieChart } from 'echarts/charts';
import { 
  GridComponent, 
  TooltipComponent, 
  TitleComponent,
  LegendComponent,
  DatasetComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import VChart from 'vue-echarts';

use([
  BarChart, 
  PieChart,
  GridComponent, 
  TooltipComponent, 
  TitleComponent,
  LegendComponent,
  DatasetComponent,
  CanvasRenderer
]);

const store = useAllDataStore();
defineEmits(['date-change']);

const salesPieChart = computed(() => {
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: RM{c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: store.state.analyticData.productSalesShare.map(p => p.product)
    },
    color: Object.values(store.productColorMap),
    series: [{
      name: 'Sales Share',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: store.state.analyticData.productPercentage.map(p => ({
        value: p.percentage,
        name: p.product,
        itemStyle: { color: store.productColorMap[p.product] }
      }))
    }]
  };
});

const productIncreaseChart = computed(() => {
  return {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}%',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: store.state.analyticData.productPercentage.map(p => p.product),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: 'Percentage (%)',
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [{
      name: 'Sales Increase',
      type: 'bar',
      data: store.state.analyticData.productPercentage.map(p => p.percentage),
      itemStyle: {
        color: (params) => {
          const productName = store.state.analyticData.productPercentage[params.dataIndex]?.product;
          return store.productColorMap[productName] || '#999';
        }
      },
      label: {
        show: true,
        position: 'top',
        formatter: '{c}%'
      }
    }],
    grid: {
      containLabel: true
    }
  };
});
</script>

<style scoped>
.analytic-data {
  margin-bottom: 40px;
}

.date-filter {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
  align-items: center;
  background: #f5f7fa;
  padding: 15px;
  border-radius: 8px;
}

.date-filter .el-select {
  width: 200px;
}

.sales-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.sales-card {
  text-align: center;
  padding: 20px;
  transition: transform 0.3s;
}

.sales-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}

.card-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin: 40px 0;
}

.chart-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 100%;
  border: 1px solid #ebeef5;
}

.chart {
  height: 400px;
  width: 100%;
}

@media (max-width: 992px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
  
  .sales-cards {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  }
  
  .date-filter {
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .date-filter .el-select {
    width: 100%;
  }
  
  .date-filter .el-button {
    width: 100%;
  }
}
</style>