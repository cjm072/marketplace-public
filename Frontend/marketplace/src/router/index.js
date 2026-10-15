import {createRouter, createWebHashHistory} from 'vue-router';

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path:'/main',
        name:'main',
        component: () => import("@/views/Main.vue"),
    },
    {
        path: '/product',
        name: 'product',
        component: () => import("@/views/Product.vue"),
    },
    {
        path: '/review',
        name: 'review',
        component: () => import("@/views/Review.vue"),
    },
    {
        path: '/cart',
        name: 'cart',
        component: () => import("@/views/Cart.vue"),
    },
    {
        path: '/checkout',
        name: 'checkout',
        component: () => import("@/views/Checkout.vue"),
    },
    {
        path: '/user',
        name: 'user',
        component: () => import("@/views/User.vue"),
    },
    {
        path: '/order',
        name: 'order',
        component: () => import("@/views/Order.vue"),
    },
    {
        path: '/seller/product',
        name: 'sellerProduct',
        component: () => import("@/views/SellerProduct.vue"),
    },
    {
        path: '/seller/sales',
        name: 'sellerSales',
        component: () => import("@/views/SellerSales.vue"),
    },
    {
        path: '/admin/sales',
        name: 'adminSales',
        component: () => import("@/views/AdminSales.vue"),
    },
    {
        path: '/admin/request',
        name: 'adminRequest',
        component: () => import("@/views/AdminRequest.vue"),
    },
    {
        path: '/admin/complaint',
        name: 'adminComplaint',
        component: () => import("@/views/AdminComplaint.vue"),
    },
    {
        path: '/successOrder',
        name: 'successOrder',
        component: () => import("@/views/SuccessOrder.vue"),
    },
    {
        path: '/login',
        name: 'login',
        component: () => import("@/views/Login.vue"),
    },
    {
        path: '/register',
        name: 'register',
        component: () => import("@/views/Register.vue"),
    },
    {
        path: '/invalidAccount',
        name: 'invalidAccount',
        component: () => import("@/views/InvalidAccount.vue"),
    },
    {
        path: '/poster',
        name: 'Poster',
        component: () => import('@/views/Poster.vue')
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;