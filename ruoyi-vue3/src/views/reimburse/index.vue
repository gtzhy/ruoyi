<template>
    <div class="reimburse-container">
        <el-row :gutter="24">
            <el-col :span="22"><el-autocomplete ref="autocompleteRef" v-model="state"
                    :fetch-suggestions="querySearchAsync" :trigger-on-focus="true" clearable placeholder="Please input"
                    @select="handleSelect" style="flex: 1;" /></el-col>
            <el-col :span="1"><el-button @click="getCurrentValue" type="primary" :icon="Search" circle /></el-col>
        </el-row>

        <!-- 添加排序按钮组 -->
        <div class="table-controls" style="margin: 10px 0; display: none;">
            <span>调整列顺序：</span>
            <el-button size="small" @click="moveColumn('id', -1)"
                :disabled="getColumnIndex('id') === 0">单据编号↑</el-button>
            <el-button size="small" @click="moveColumn('date', -1)"
                :disabled="getColumnIndex('date') === 0">单据类型↑</el-button>
            <el-button size="small" @click="moveColumn('name', -1)"
                :disabled="getColumnIndex('name') === 0">差旅类型↑</el-button>
            <el-button size="small" @click="moveColumn('address', -1)"
                :disabled="getColumnIndex('address') === 0">申请人↑</el-button>
            <el-button size="small" @click="resetColumnOrder">重置顺序</el-button>
        </div>

        <div class="table-message" style="white-space: pre-line;">
            {{ tableMessage }}
        </div>

        <el-table :data="tableData" style="width: 100%; margin-bottom: 20px" row-key="id" border default-expand-all>
            <el-table-column v-for="column in orderedColumns" :key="column.prop" :prop="column.prop"
                :label="column.label" :sortable="column.sortable" :fixed="column.fixed">
                <template #default="scope" v-if="column.prop === 'billId'">
                    <el-button link type="primary" size="small" @click="handleButtonClick(scope.row)">
                        {{ scope.row.billId }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { getUserInfo, getUserTravelBill } from '@/api/reimburse/user'
import { ElLoading } from 'element-plus'

const tableData = ref([])

const tableMessage = ref('');

// 定义所有列的初始顺序
const allColumns = [
    { prop: 'billId', label: '单据编号', sortable: false },
    { prop: 'eventName', label: '单据类型', sortable: false },
    { prop: 'detail', label: '差旅类型', sortable: false },
    { prop: 'billSubmitTime', label: '提交日期', sortable: false },
    { prop: 'billFinishTime', label: '审批完结时间', sortable: false },
    { prop: 'payFinishTime', label: '支付日期', sortable: false },
    { prop: 'billAmount', label: '金额', sortable: false },
    { prop: 'payStatus', label: '付款状态', sortable: false },
    { prop: 'billStatus', label: '单据状态', sortable: false },
];

// 使用响应式变量存储当前列顺序
const columnOrder = ref([0, 1, 2, 3, 4, 5, 6]); // 初始顺序

// 计算属性：返回按当前顺序排列的列
const orderedColumns = computed(() => {
    //   return columnOrder.value.map(index => allColumns[index]);
    return allColumns;
});

const handleButtonClick = (row) => {
    let url = `https://xft.cmbchina.com/TripMainWeb/#/trip-app/billDetail?billId=${row.billId}&viewType=ADJUST&reserveTab=true&tabTitle=%E5%8D%95%E6%8D%AE%E8%AF%A6%E6%83%85-${row.billId}&accountVisibleEntry=other`
    window.open(url, '_blank');
};

// 移动列位置
const moveColumn = (prop, direction) => {
    const currentIndex = getColumnIndex(prop);
    if (currentIndex === -1) return;

    const targetIndex = currentIndex + direction;
    if (targetIndex < 0 || targetIndex >= allColumns.length) return;

    // 交换数组中的位置
    const newOrder = [...columnOrder.value];
    [newOrder[currentIndex], newOrder[targetIndex]] = [newOrder[targetIndex], newOrder[currentIndex]];
    columnOrder.value = newOrder;
};

// 获取指定属性列的当前索引
const getColumnIndex = (prop) => {
    return orderedColumns.value.findIndex(column => column.prop === prop);
};

// 重置列顺序为默认
const resetColumnOrder = () => {
    columnOrder.value = [0, 1, 2, 3, 4, 5, 6]; // 恢复原始顺序
};

const state = ref('')
const autocompleteRef = ref()
let userName = null;

const querySearchAsync = (_queryString, cb) => {
    if (userName == null) {
        cb([]);
        return;
    }
    getUserInfo({
        userName: userName
    }).then(res => {
        const data = res.data;
        const arr = data.result;
        let results = arr.map((item) => {
            return { 
                value: item.stfName + ' - ' + item.stfNumber, 
                stfName: item.stfName, 
                stfNumber: item.stfNumber,
                householdAddressCity: item.householdAddressCity,
                householdAddressDetail: item.householdAddressDetail,
                householdAddressDistrict: item.householdAddressDistrict,
                householdAddressProvince: item.householdAddressProvince,
                presentAddressCity: item.presentAddressCity,
                presentAddressDetail: item.presentAddressDetail,
                presentAddressDistrict: item.presentAddressDistrict,
                presentAddressProvince: item.presentAddressProvince,
                entryDate: item.entryDate,
                actualPositiveDate: item.actualPositiveDate
            }
        })
        cb(results);
    })
    userName = null;
}

const handleSelect = (item) => {
    const typedItem = item;
    state.value = typedItem.stfName;

    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    debugger
    getUserTravelBill({
        userName: typedItem.stfNumber
    }).then(res => {
        loading.close();
        const data = res.data;
        let arr = data.baseInfo;
        let detailInfo = data.detailInfo;
        let payInfo = data.payInfo;
        let countLeave = 0;
        for (let i = 0; i < arr.length; i++) {
            let key = arr[i].billId;
            arr[i].detail = detailInfo[key];

            if (detailInfo[key] == "探亲假（无住宿报销）" && arr[i].billStatus == "审批通过") {
                countLeave++;
            }

            let str = payInfo[key];
            if (str == null) {
                arr[i].payFinishTime = "未知";
                arr[i].payStatus = "未知";
            } else {
                let payInfoArr = str.split(",");
                arr[i].payFinishTime = payInfoArr[0] === "null" ? "未知" : payInfoArr[0];
                switch (payInfoArr[1]) {
                    case "UNPAID":
                        arr[i].payStatus = "未支付";
                        break;
                    case "IN_PAYMENT":
                        arr[i].payStatus = "支付中";
                        break;
                    case "PAY_SUCCESS":
                        arr[i].payStatus = "支付成功";
                        break;
                    default:
                        arr[i].payStatus = "未知";
                        break;
                }
            }

        }

        tableData.value = arr;
        const householdAddr = [
            typedItem.householdAddressProvince,
            typedItem.householdAddressCity,
            typedItem.householdAddressDistrict,
            typedItem.householdAddressDetail
        ].filter(Boolean).join('');
        const presentAddr = [
            typedItem.presentAddressProvince,
            typedItem.presentAddressCity,
            typedItem.presentAddressDistrict,
            typedItem.presentAddressDetail
        ].filter(Boolean).join('');

        tableMessage.value =
            `户籍地址：${householdAddr}
        居住地址：${presentAddr}
        入职时间：${typedItem.entryDate}
        转正时间：${typedItem.actualPositiveDate}
        本年度已通过${countLeave}次返乡探亲休假，每季度一次，每年共四次（含首次报道）。新员工休假按入职时间起以季度为单位进行休假。
    `;

    }).catch(err => {
        loading.close();
        console.error('There was an error!', err);
    })
}

const getCurrentValue = () => {
    userName = state.value;
    // 使用组件的 ref 来访问其内部方法
    if (autocompleteRef.value) {
        // 触发组件内部的建议刷新机制
        // 注意：不同的 Element Plus 版本可能 API 有所不同
        if (autocompleteRef.value.refetchSuggestions) {
            autocompleteRef.value.refetchSuggestions();
        } else {
            // 如果没有 refetchSuggestions 方法，可以尝试其他方式
            if (autocompleteRef.value.focus) {
                autocompleteRef.value.focus()
            }
        }
    }
}
</script>
<style scoped>
.table-message {
    font-size: 14px;
    color: #606266;
    margin: 10px 0;
    min-height: 14px;
}
</style>
