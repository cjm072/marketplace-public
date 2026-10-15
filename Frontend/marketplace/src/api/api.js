import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

const instance = axios.create({
});

instance.interceptors.request.use(
  (config) => {
    const publicEndpoints = ['/account/login', '/customer/account/register', '/seller/account/register'];
    const isPublic = publicEndpoints.some(path => config.url.includes(path));

    if (!isPublic) {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default{
    getProductById(productId){
        return instance.get(`${BASE_URL}/global/product/${productId}`)
    },
    getProductsByName(name) {
        return instance.get(`${BASE_URL}/global/product/searchByName`, {
            params: { name }
        });
    },
    getAllProducts(){
        return instance.get(`${BASE_URL}/customer/product/batch`)
    },
    getAllSellerProducts(sellerId){
        return instance.get(`${BASE_URL}/seller/product/batch/${sellerId}`)
    },
    getAllApprovedProducts(){
        return instance.get(`${BASE_URL}/customer/product/batch/approvedProducts`)
    },
    getAllProductsBySeller(sellerId){
        return instance.get(`${BASE_URL}/seller/product/batch/${sellerId}`)
    },
    addProduct(product){
        return instance.post(`${BASE_URL}/seller/product`, product)
    },
    updateProduct(product, productId){
        return instance.put(`${BASE_URL}/seller/product/${productId}`, product)
    },
    deleteProduct(productId){
        return instance.delete(`${BASE_URL}/seller/product/${productId}`)
    },
    applyDiscountToProduct(productId, discountId){
        return instance.put(`${BASE_URL}/seller/product/${productId}/applyDiscount/${discountId}`)
    },
    removeDiscountFromProduct(productId, discountId){
        return instance.put(`${BASE_URL}/seller/product/${productId}/removeDiscount/${discountId}`)
    },
    addCart(cart){
        return instance.post(`${BASE_URL}/customer/cart`, cart)
    },
    addProductToCart(cartItem){
        return instance.patch(`${BASE_URL}/customer/cart/addToCart`, cartItem)
    },
    deleteCartItemById(cartItemId, cartId){
        return instance.delete(`${BASE_URL}/customer/cart/deleteFromCart/${cartItemId}/${cartId}`)
    },
    deleteAllCartItems(cartId){
        return instance.delete(`${BASE_URL}/customer/cart/deleteAllFromCart/${cartId}`)
    },
    getCartById(cartId){
        return instance.get(`${BASE_URL}/customer/cart/${cartId}`)
    },
    getDeliveryFee(cartId, deliveries){
        const query = deliveries.map(d => `deliveries=${d}`).join('&');
        return instance.get(`${BASE_URL}/customer/cart/deliveryFee/${cartId}?${query}`);
    },
    updateCartItemQuantity(quantity, cartItemId) {
        return instance.patch(`${BASE_URL}/customer/cart/updateQuantity/${quantity}/${cartItemId}`);
    },
    getAllVouchers(){
        return instance.get(`${BASE_URL}/global/voucher/batch`)
    },
    addVoucher(voucher){
        return instance.post(`${BASE_URL}/admin/voucher`, voucher)
    },
    updateVoucher(voucher, voucherId){
        return instance.put(`${BASE_URL}/admin/voucher/${voucherId}`, voucher)
    },
    deleteVoucher(voucherId){
        return instance.delete(`${BASE_URL}/admin/voucher/${voucherId}`)
    },
    addOrder(order){
        return instance.post(`${BASE_URL}/customer/order`, order)
    },
    getAllComplaints(){
        return instance.get(`${BASE_URL}/admin/complaint/batch`)
    },
    deleteComplaintById(complaintId){
        return instance.delete(`${BASE_URL}/admin/complaint/${complaintId}`)
    },
    rejectSeller(sellerId){
        return instance.patch(`${BASE_URL}/admin/account/reject/${sellerId}`)
    },
    getAllPendingSellers(){
        return instance.get(`${BASE_URL}/admin/account`)
    },
    approveSeller(sellerId){
        return instance.patch(`${BASE_URL}/admin/account/approve/${sellerId}`)
    },
    getAllPendingProducts(){
        return instance.get(`${BASE_URL}/admin/product`)
    },
    approveProduct(productId){
        return instance.patch(`${BASE_URL}/admin/product/approve/${productId}`)
    },
    rejectProduct(productId){
        return instance.patch(`${BASE_URL}/admin/product/reject/${productId}`)
    },
    getAllDiscounts(sellerId){
        return instance.get(`${BASE_URL}/seller/discount/batch/${sellerId}`)
    },
    addDiscount(discount){
        return instance.post(`${BASE_URL}/seller/discount`, discount)
    },
    updateDiscount(discount, discountId){
        return instance.put(`${BASE_URL}/seller/discount/${discountId}`, discount)
    },
    deleteDiscount(discountId){
        return instance.delete(`${BASE_URL}/seller/discount/${discountId}`)
    },
    getCustomerById(customerId){
        return instance.get(`${BASE_URL}/customer/account/${customerId}`)
    },
    getSellerById(sellerId){
        return instance.get(`${BASE_URL}/seller/account/${sellerId}`)
    },
    getAdminById(adminId){
        return instance.get(`${BASE_URL}/account/${adminId}`)
    },
    updateProfile(userId, profile){
        return instance.patch(`${BASE_URL}/account/profileInfo/${userId}`, profile)
    },
    updatePassword(userId, password){
        return instance.patch(`${BASE_URL}/account/updatePassword/${userId}`, password)
    },
    updateAddress(userId, address){
        return instance.patch(`${BASE_URL}/account/updateAddress/${userId}`, address)
    },
    getAllOrdersByCustomer(customerId){
        return instance.get(`${BASE_URL}/customer/order/customer/${customerId}`)
    },
    getAllReviewsByProduct(productId){
        return instance.get(`${BASE_URL}/review/batch/${productId}`)
    },
    addReview(review){
        return instance.post(`${BASE_URL}/customer/review`,review)
    },
    addComplaint(complaint){
        return instance.post(`${BASE_URL}/customer/complaint`,complaint)
    },
    getPaymentCountPerMonth(year) {
        return instance.get(`${BASE_URL}/admin/payment/countByMonth?year=${year}`);
    },
    getPaymentAmountPerMonth(year) {
        return instance.get(`${BASE_URL}/admin/payment/amountByMonth?year=${year}`);
    },
    getAveragePaymentPerMonth(year) {
        return instance.get(`${BASE_URL}/admin/payment/averageByMonth?year=${year}`);
    },
    getCategorySalesPercentage(year, month) {
        return instance.get(`${BASE_URL}/admin/payment/categorySalesPercentage`, {
            params: { year, month }
        });
    },
    getCategorySalesChange(year, month) {
        return instance.get(`${BASE_URL}/admin/payment/categorySalesChange`, {
            params: { year, month }
        });
    },
    getSellerPaymentCountPerMonth(year, sellerId) {
        return instance.get(`${BASE_URL}/seller/payment/countByMonth`, {
            params: { year, sellerId }
        });
    },

    getSellerPaymentAmountPerMonth(year, sellerId) {
        return instance.get(`${BASE_URL}/seller/payment/amountByMonth`, {
            params: { year, sellerId }
        });
    },

    getSellerAveragePaymentPerMonth(year, sellerId) {
        return instance.get(`${BASE_URL}/seller/payment/averageByMonth`, {
            params: { year, sellerId }
        });
    },

    getSellerProductSalesPercentage(year, month, sellerId) {
        return instance.get(`${BASE_URL}/seller/payment/productSalesPercentage`, {
            params: { year, month, sellerId }
        });
    },

    getSellerProductSalesChange(year, month, sellerId) {
        return instance.get(`${BASE_URL}/seller/payment/productSalesChange`, {
            params: { year, month, sellerId }
        });
    },
    uploadProductPicture(file, productId) {
        const formData = new FormData()
        formData.append("image", file)
        formData.append("productId", productId)
        return instance.put(`${BASE_URL}/seller/product/uploadProductPicture`, formData, {
            headers: {
            "Content-Type": "multipart/form-data"
            }
        })
    },
    uploadProfilePicture(file, userId) {
        const formData = new FormData()
        formData.append("image", file)
        formData.append("userId", userId)
        return instance.put(`${BASE_URL}/account/uploadProfilePicture`, formData, {
            headers: {
            'Content-Type': 'multipart/form-data',
            },
        });
    },
    registerCustomer(customer){
        return instance.post(`${BASE_URL}/customer/account/register`, customer)
    },
    registerSeller(seller){
        return instance.post(`${BASE_URL}/seller/account/register`, seller)
    },
    login(credentials){
        return instance.post(`${BASE_URL}/account/login`, credentials)
    },
    checkSellerStatus(sellerId){
        return instance.get(`${BASE_URL}/account/checkSellerStatus/${sellerId}`)
    },
}