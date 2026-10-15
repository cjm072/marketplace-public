import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

function initState() {
  return {
    userId: null,
    userRole: '',
    searchQuery: '',
    selectedTab: 'all',
    isAdmin: false,
    selectedProductId: null,
    currentCartId: 1,
    cartDeliveryOptions: {}, 
    successOrder: null,
    analyticData: {
      selectedYear: new Date().getFullYear(),
      selectedMonth: new Date().getMonth() + 1,
      salesData: {
        orders: 0,
        sales: 0,
        average: 0
      },
      productPercentage: [],
      productSalesShare: [],
      availableYears: [2022, 2023, 2024, 2025],
      months: [
        'January', 'February', 'March', 'April', 
        'May', 'June', 'July', 'August',
        'September', 'October', 'November', 'December'
      ],
      colorPalette: [
        '#5470C6', '#81CD35', '#DAA958', '#EE6566', 
        '#73C0DE', '#3BA272', '#CF6801', '#9A60B4',
        '#FF9F7F', '#C1232B', '#27727B'
      ]
    }
  };
}

export const useAllDataStore = defineStore('allData', () => {
  const state = ref(initState());

  const setIsAdmin = (value) => {
    state.value.isAdmin = value;
  };

  const selectedMonthName = computed(() => {
    return state.value.analyticData.months[state.value.analyticData.selectedMonth - 1];
  });

  const productColorMap = computed(() => {
    const map = {};
    const analyticData = state.value.analyticData;
    const allProducts = [
      ...analyticData.productSalesShare.map(p => p.product),
      ...analyticData.productPercentage.map(p => p.product)
    ];

    const uniqueProducts = [...new Set(allProducts)];

    uniqueProducts.forEach((product, index) => {
      map[product] = analyticData.colorPalette[index % analyticData.colorPalette.length];
    });

    return map;
  });


  const setSalesData = (data) => {
    state.value.analyticData.salesData.orders = data.orders;
    state.value.analyticData.salesData.sales = data.sales;
    state.value.analyticData.salesData.average = data.average;
  };
  
  const setProductData = (percentageData, salesShareData) => {
    state.value.analyticData.productPercentage = percentageData;
    state.value.analyticData.productSalesShare = salesShareData;
  };
  
  const setDateFilter = (year, month) => {
    state.value.analyticData.selectedYear = year;
    state.value.analyticData.selectedMonth = month;
  };

  const initializeDeliveryOptions = (sellerIds) => {
    sellerIds.forEach(id => {
      if (!state.value.cartDeliveryOptions[id]) {
        state.value.cartDeliveryOptions[id] = 'standard';
      }
    });
  };

  const setDeliveryOption = (sellerId, method) => {
      state.value.cartDeliveryOptions[sellerId] = method;
  };

  return {
    state,
    setIsAdmin,
    selectedMonthName,
    productColorMap,
    setSalesData,
    setProductData,
    setDateFilter,
    initializeDeliveryOptions,
    setDeliveryOption, 
  };
});