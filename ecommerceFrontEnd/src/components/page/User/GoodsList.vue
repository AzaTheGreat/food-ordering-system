<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item>Product list</el-breadcrumb-item>
    </el-breadcrumb>
    <br />
    <br />

    <span>
      Search：
      <el-input style="width:15rem" placeholder="Product name" @input="search" v-model="search_name"></el-input>
    </span>
    <el-select
      style="width:15rem"
      placeholder="Product type"
      v-model="type"
      autocomplete="off"
      @change="search"
      clearable
    >
      <el-option
        v-for="item in type_options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>
    <el-button type="#5AB9EA" icon="el-icon-search"></el-button>
    <el-divider></el-divider>
    <el-row>
      <el-col :span="4" v-for="item in goods" :key="item.title">
        <div>
          <el-card class="goods-card" :body-style="{ padding: '0px' }">
            <img :src="item.remark" class="small-img" @click="detail(item)" />
            <div class="goods-info-div">
              <div class="goods-row">
                <span>{{item.title}}</span>
                <div class="small-icon" @click="star(item)">
                  <i :class="item.star == 1 ? 'el-icon-star-on' :'el-icon-star-off'"></i>
                </div>
              </div>
              <div class="goods-row">
                <el-link :underline="false" type="danger">¥ {{item.retail_price}}</el-link>
                <el-button type="text" class="btn" @click="detail(item)">Details</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <el-drawer title="drawer" :visible.sync="drawer" size="50%" :with-header="false">
      <div class="goods-div">
        <h2>{{chooseItem.title}}</h2>
        <div class="icon" @click="star(chooseItem)">
          <i :class="chooseItem.star == 1 ? 'el-icon-star-on' :'el-icon-star-off'"></i>
        </div>
        <div class="base-info">
          <img :src="chooseItem.remark" class="img" />
          <div style="width:25rem">
            <p>{{chooseItem.title}}</p>
            <el-link :underline="false" type="danger">{{chooseItem.retail_price}} ¥</el-link>
            <p>Restaurant: {{chooseItem.name_en}}</p>
            <el-popover placement="bottom" v-model="dialogVisible">
              <div class="check">
                <el-checkbox
                  :indeterminate="isIndeterminate"
                  v-model="checkAll"
                  @change="handleCheckAllChange"
                >Amazon</el-checkbox>

                <el-checkbox-group v-model="checkedAStores" @change="handleCheckedAStoresChange">
                  <el-checkbox
                    class="store"
                    v-for="item in Astores"
                    :label="item"
                    :key="item"
                  >{{item}}</el-checkbox>
                </el-checkbox-group>

                <el-checkbox
                  class="title"
                  :indeterminate="EisIndeterminate"
                  v-model="EcheckAll"
                  @change="EhandleCheckAllChange"
                >Ebay</el-checkbox>

                <el-checkbox-group v-model="checkedEStores" @change="handleCheckedEStoresChange">
                  <el-checkbox
                    class="store"
                    v-for="item in Estores"
                    :label="item"
                    :key="item"
                  >{{item}}</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="minibtn">
                <el-button size="mini" @click="dialogVisible = false">Cancel</el-button>
                <el-button size="mini" type="primary" plain @click="pushShip">Push</el-button>
              </div>
              <el-button type="primary" slot="reference">Order Now</el-button>
            </el-popover>
            <el-button
              type="#5AB9EA"
              @click="star(chooseItem)"
            >{{chooseItem.star == 1 ? 'Remove from Wish List' : 'Add to Wish List'}}</el-button>
          </div>
        </div>
        <br />
        <el-tabs v-model="activeName" class="tab">
          <el-tab-pane label="Description" name="first">
            <article v-html="compileMarkDown(chooseItem.amazon_description)" ></article> 
          </el-tab-pane>
          <el-tab-pane label="ebay description" name="second">
            <article v-html="compileMarkDown(chooseItem.ebay_description)" ></article>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import showdown from 'showdown'
var converter = new showdown.Converter();
export default {
  data() {
    return {
      checkAll: false,
      EcheckAll: false,
      checkedAStores: [],
      checkedEStores: [],
      Astores: [],
      Estores: [],
      isIndeterminate: true,
      EisIndeterminate: true,
      //
      dialogVisible: false,
      activeName: "first",
      chooseItem: {},
      drawer: false,
      type_options: [
        { value: "Phone", label: "Phone" },
        { value: "Computer", label: "Computer" },
        { value: "Instrument", label: "Instrument" },
        { value: "Appliances", label: "Appliances" }
      ],
      search_name: "",
      type: "",
      goods: [],
      table: [],
      cart: []
    };
  },
  mounted() {
    if(sessionStorage.getItem("user_id") != 3){
      this.$post("/str/getStoreByUserID", {
      user_id: sessionStorage.getItem("user_id")
    }).then(res => {
      if (res.code == 504) {
        this.$message.warning(res.message);
        return;
      }
      if (res.code == 200) {
        console.log(res.data);
        this.Astores = res.data
          .filter(e => e.plataeform_type == 1)
          .map(e => e.store_name);
        this.Estores = res.data
          .filter(e => e.plataeform_type == 2)
          .map(e => e.store_name);
      }
    });
    }
    
    this.$post("/product/getProductOnShelf", {
      user_id: sessionStorage.getItem("user_id")
    }).then(res => {
      if (res.code == 504) {
        this.$notify.warning(res.message);
        return;
      }
      if (res.code == 200) {
        this.goods = res.data;
        this.table = res.data;
      }
    });
  },
  methods: {
    compileMarkDown(value) {
      return converter.makeHtml(value)
    },
    refresh() {
      this.$post("/product/getProductOnShelf", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        if (res.code == 504) {
          this.$notify.warning(res.message);
          return;
        }
        if (res.code == 200) {
          this.goods = res.data;
          this.table = res.data;
        }
      });
    },
    EhandleCheckAllChange(val) {
      this.checkedEStores = val ? this.Estores : [];
      this.EisIndeterminate = false;
    },
    handleCheckAllChange(val) {
      this.checkedAStores = val ? this.Astores : [];
      this.isIndeterminate = false;
    },
    handleCheckedEStoresChange(value) {
      let checkedCount = value.length;
      this.EcheckAll = checkedCount === this.Estores.length;
      this.EisIndeterminate =
        checkedCount > 0 && checkedCount < this.Estores.length;
    },
    handleCheckedAStoresChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.Astores.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.Astores.length;
    },
    pushShip() {
      this.dialogVisible = false;
      this.$notify.success("Successfull");
    },

    async getCart() {
      await this.$post("/cart/getCart", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        let temp = [];
        for (var i = 0; i < res.data.number; i++) {
          temp.push({
            name: res.data.product[i].title,
            src: res.data.product[i].remark,
            price: res.data.product[i].retail_price,
            brand: res.data.brand[i].name_en,
            stock: res.data.product[i].replenishment_period,
            amazondescription: res.data.packageinfo[i].amazon_description,
            ebaydescription: res.data.packageinfo[i].ebay_description,
            witid: res.data.cart[i].wit_id,
            star: 1,
            proid: res.data.product[i].pro_id
          });
        }
        this.cart = temp;
      });
    },
    star(item) {
      this.getCart().then(r => {
        var witid;
        for (var i = 0; i < this.cart.length; i++) {
          if (item.pro_id == this.cart[i].proid) {
            witid = this.cart[i].witid;
          }
        }
        if (item.star == 1) {
          this.$post("/cart/deletedCart", {
            wit_id: witid
          }).then(res => {
            item.star = 2;
            this.refresh();
          });
          return;
        } else {
          this.$post("/cart/addCart", {
            user_id: sessionStorage.getItem("user_id"),
            pro_id: item.pro_id
          }).then(res => {
            item.star = 1;
            this.refresh();
          });
        }
      });
    },
    detail(item) {
      this.chooseItem = item;
      this.drawer = true;
    },
    search() {
      this.goods = this.table.filter(
        e => e.title.match(this.search_name) && e.key_words.match(this.type)
      );
    }
  }
};
</script>

<style scoped>
.minibtn {
  margin-top: 1rem;
}
.check >>> .el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #5AB9EA;
  border-color: #5AB9EA;
}
.check >>> .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #5AB9EA;
  border-color: #5AB9EA;
}
.check >>> .el-checkbox__input.is-checked + .el-checkbox__label {
  color: #5AB9EA;
}
.title {
  margin-top: 0.5rem;
}
.store {
  display: block;
  margin-left: 1rem;
  margin-top: 0.5rem;
}
.tab {
  width: 50rem;
}
.tab >>> .el-tabs__item.is-active {
  color: #5AB9EA;
}
.tab >>> .el-tabs__active-bar {
  background-color: #5AB9EA;
}
.base-info {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.goods-div {
  margin-left: 3rem;
  height: 60rem;
}
.small-icon {
  font-size: 20px;
  color: #f3c93f;
}
.icon {
  position: absolute;
  top: 1rem;
  left: 11rem;
  font-size: 30px;
  color: #f3c93f;
}
.goods-row {
  width: 94%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.goods-info-div {
  margin-top: 0.5rem;
  margin-left: 1rem;
}
.small-img {
  width: 100%;
  height: 15rem;
}
.img {
  width: 20rem;
  height: 20rem;
}
.goods-card {
  margin-top: 0.8rem;
  width: 15rem;
  height: 20rem;
}
</style>