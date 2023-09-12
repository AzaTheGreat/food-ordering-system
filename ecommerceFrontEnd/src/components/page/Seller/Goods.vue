<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item>Products</el-breadcrumb-item>
    </el-breadcrumb>
    <br />
    <br />

    <span>
      Search：
      <el-input
        style="width:200px"
        placeholder="Product title"
        @input="search"
        v-model="search_goodstitle"
      ></el-input>
    </span>
    <el-button type="primary" icon="el-icon-search"></el-button>
    <el-button type="primary" plain icon="el-icon-plus" @click="add">Add</el-button>
    <el-divider></el-divider>
    <el-table
      :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)"
      style="width: 100%"
      class="table-check"
      @selection-change="handleSelectionChange"
      height="550"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="title" label="Product title"></el-table-column>
      <el-table-column
        prop="key_words"
        label="Product type"
        :filter-method="filterHandler"
        :filters="[{ text: 'Burger', value: 'Burger' },{ text: 'Salad', value: 'Salad' }, { text: 'Pizza', value: 'Pizza' }]"

      ></el-table-column>
      <el-table-column prop="remark" label="Product picture">
        <template slot-scope="scope">
          <img :src="scope.row.remark" width="100" />
        </template>
      </el-table-column>
      <el-table-column prop="retail_price" label="Product price"></el-table-column>

      <el-table-column
        prop="sts_cd"
        label="State"
        :filter-method="filterHandler"
        :filters="[{ text: 'Not in warehouse', value: 'Not in warehouse' },{ text: 'In warehouse', value: 'In warehouse' }, { text: 'On shelf', value: 'On shelf'}]"
      >
        <template slot-scope="scope">
          <el-tag :type="tag(scope.row.sts_cd)" disable-transitions>{{scope.row.sts_cd}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="operation" width="250">
        <template slot-scope="scope">
          <el-button type="success" size="small" icon="el-icon-edit" @click="edit(scope.row)"></el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="remove(scope.row, scope.$index)"
          ></el-button>
          <el-button
            :type="btntag(scope.row.sts_cd)"
            size="small"
            @click="operate(scope.row)"
          >{{btn(scope.row.sts_cd)}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br>
     <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[6,12,tableData.length]"
      :page-size="pagesize"
      layout="total,sizes,prev,pager,next,jumper"
      :total="tableData.length"
    ></el-pagination>
    <br />
    <span>Batch：</span>
    <el-button @click="batch(msg[0])" type="primary">Push</el-button>
    <el-button @click="batch(msg[1])" type="success">Shelve</el-button>
    <el-button @click="batch(msg[2])" type="warning">Unshelve</el-button>
    <el-drawer title="drawer" :visible.sync="drawer" size="50%" :with-header="false" class="slip">
      <div class="form-div">
        <h3>Product information</h3>
        <el-form
          :model="addGoodsForm"
          ref="addGoodsForm"
          label-width="150px"
          class="add-goods-form"
        >
          <el-form-item
            label="Product title"
            prop="title"
            :rules="[{ required: true, message: 'Please enter the product title'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.title" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item label="Product brand" prop="brand_options">
            <!-- :rules="[{ required: true, message: 'Please choose the Product brand', trigger: 'blur'}]" -->

            <el-select
              style="width:35rem"
              placeholder
              v-model="addGoodsForm.brd_id"
              autocomplete="off"
            >
              <el-option
                v-for="item in brand_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <br />

          <el-form-item
            label="Product type"
            prop="key_words"
            :rules="[{ required: true, message: 'Please choose the Product type', trigger: 'blur'}]"
          >
            <el-select
              style="width:35rem"
              placeholder
              v-model="addGoodsForm.key_words"
              autocomplete="off"
            >
              <el-option
                v-for="item in type_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <br />

          <el-form-item
            label="Product price"
            prop="retail_price"
            :rules="[{ required: true, message: 'Please enter the Product price'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.retail_price" autocomplete="off"></el-input>
          </el-form-item>

          <br />
          <el-form-item
            label="Product shelf life"
            prop="warranty_day"
            :rules="[{ required: true, message: 'Please enter the product shelf life'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.warranty_day" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <div class="flex">
            <div>
              <span>Product picture:</span>
              <br />
              <br />
              <el-upload
                style="width:24rem"
                ref="upload"
                drag
                action="http://localhost:8088/product/uploadPicture"
                :name="fileName"
                :file-list="fileList"
                :on-success="handleSuccess"
                :on-change="fileChange"
                :before-upload="uploadPicture"
                :auto-upload="false"
                list-type="picture"
                :limit="1"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  Drag the file here, or
                  <em>Click to upload</em>
                </div>
                <div
                  class="el-upload__tip"
                  slot="tip"
                >It is recommended to upload JPG/PNG files and not exceed 500kb</div>
              </el-upload>
            </div>

          </div>

          <br />
          <span>Description：</span>
          <mavon-editor
            :subfield="false"
            :toolbars="toolbars"
            class="markdown"
            v-model="addGoodsForm.amazon_description"
            ref="md"
          />

          <el-form-item class="goods-form-button">
            <el-button type="primary" @click="submitForm('addGoodsForm')">Save</el-button>
            <el-button @click="darCancel('addGoodsForm')">Cancel</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      toolbars: {
        bold: true,
        italic: true,
        header: true,
        underline: true,
        mark: true,
        ol: true,
        ul: true,
        link: true,
        imagelink: true,
        code: false,
        fullscreen: true,
        readmodel: true,
        htmlcode: true,
        /* 1.3.5 */
        undo: true,
        redo: true,
        trash: true,
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      },
      count: 0,
      fileList: [],
      fileName: "fileName",
      isAdd: false,
      currentPage: 1,
      pagesize: 5,
      // brand_options: [{ value: 1, label: "p" }],
      brand_options: [],
      type_options: [
        { value: "Burger", label: "Burger" },
        { value: "Salad", label: "Salad" },
        { value: "Pizza", label: "Pizza" },
      ],
      multipleSelection: [],
      msg: ["Not in warehouse", "In warehouse", "On shelf"],
      addGoodsForm: {
        pro_id: "",
        title: "",
        name_en: "",
        retail_price: "",
        replenishment_period: "",
        remark: "",
        sku_cd: "",
        key_words: "",
        upc: "",
        warranty_day: "",
        ean: "",
        model: "",
        length: "",
        width: "",
        height: "",
        weight: "",
        ebay_description: "",
        amazon_description: "",
        brd_id: ""
      },
      ori_form: {
        pro_id: "",
        title: "",
        name_en: "",
        retail_price: "",
        replenishment_period: "",
        remark: "",
        sku_cd: "",
        key_words: "",
        upc: "",
        warranty_day: "",
        ean: "",
        model: "",
        length: "",
        width: "",
        height: "",
        weight: "",
        ebay_description: "",
        amazon_description: "",
        brd_id: ""
      },
      search_goodstitle: "",
      tableData: [],
      table: [],
      opRow: "",
      drawer: false
    };
  },
  mounted() {
    this.$post("brd/getBrand", {
      user_id: sessionStorage.getItem("user_id")
    }).then(res => {
      let tmpBrands = [];
      for (let i = 0; i < res.data.length; i++) {
        tmpBrands[i] = {
          value: res.data[i].brd_id,
          label: res.data[i].name_en
        };
      }
      this.brand_options = tmpBrands;
    }),
      this.$post("/product/getProducts", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          return;
        }
        if (res.code == 200) {
          this.tableData = res.data;
          this.table = res.data;
        }
      });
  },
  methods: {
    handleSizeChange(size) {

      this.pagesize = size;
    },
    handleCurrentChange(currentPage) {

      this.currentPage = currentPage;
    },
    batch(msg) {
      if (this.multipleSelection == 0) {
        this.$message.warning("Please select item");
        return;
      }
      if (
        this.multipleSelection.filter(e => e.sts_cd == msg).length !=
        this.multipleSelection.length
      ) {
        this.$message.warning(`Please select items that ${msg}`);
        return;
      }
      let nextStage = "";
      if (msg == "Not in warehouse") {
        nextStage = "In warehouse";
      }
      if (msg == "In warehouse") {
        nextStage = "On shelf";
      }
      if (msg == "On shelf") {
        nextStage = "In warehouse";
      }
      this.multipleSelection.forEach(element => {
        this.$post("/product/updateSts", {
          pro_id: element.pro_id,
          sts_cd: nextStage
        }).then(res => {
          if (res.code == 504) {
            this.$message.warning(res.message);
            return;
          }
          if (res.code == 200) {
            this.refresh();
          }
        });
      });
      this.$message.success("Success");
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    filterHandler(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    add() {
      if(sessionStorage.getItem("user_id") == 3){
        this.$message.warning("Admin can not operate")
        return
      }
      console.log(this.addGoodsForm);
      this.addGoodsForm.brd_id = "";
      // this.addGoodsForm
      this.isAdd = true;
      this.drawer = true;
    },
    remove(row, index) {
      this.$post("/product/deleteProduct", {
        pro_id: row.pro_id
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          this.refresh();
          return;
        }
        if (res.code == 200) {
          this.$message.success(res.message);
          this.refresh();
        }
      });
    },
    darCancel(formName) {
      this.addGoodsForm.ebay_description = "";
      this.addGoodsForm.amazon_description = "";
      this.$refs[formName].resetFields();
      this.drawer = false;
      this.count = 0;
      this.fileList = [];
    },
    fileChange() {
      this.count++;
      console.log("count");
      console.log(this.count);
    },
    uploadPicture(file) {
    },
    handleSuccess(res) {
      console.log("handlesuccess");
      if (this.isAdd) {
        console.log("add");
        // console.log(this.addGoodsForm.brd_id);
        this.isAdd = false;
        this.$post("/product/addProduct", {
          brd_id: this.addGoodsForm.brd_id,
          retail_price: this.addGoodsForm.retail_price,
          sku_cd: this.addGoodsForm.sku_cd,
          title: this.addGoodsForm.title,
          upc: this.addGoodsForm.upc,
          ean: this.addGoodsForm.ean,
          name_en: this.addGoodsForm.name_en,
          ebay_description: this.addGoodsForm.ebay_description,
          amazon_description: this.addGoodsForm.amazon_description,
          key_words: this.addGoodsForm.key_words,
          width: this.addGoodsForm.width,
          height: this.addGoodsForm.height,
          weight: this.addGoodsForm.weight,
          length: this.addGoodsForm.length,
          model: this.addGoodsForm.model,
          replenishment_period: this.addGoodsForm.replenishment_period,
          warranty_day: this.addGoodsForm.warranty_day,
          remark: res.data,
          user_id: sessionStorage.getItem("user_id")
        }).then(res => {
          if (res.code == 504) {
            this.$message.warning(res.message);
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
          if (res.code == 200) {
            this.$message.success(res.message);
            this.refresh();
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
        });
        this.drawer = false;
        return;
      }
      if (!this.isAdd) {
        console.log("update");
        // console.log(this.addGoodsForm.title);
        console.log(this.addGoodsForm);
        // let title = this.addGoodsForm.title;
        // console.log(title)
        this.$post("/product/updateProduct", {
          brd_id: this.addGoodsForm.brd_id,
          retail_price: this.addGoodsForm.retail_price,
          sku_cd: this.addGoodsForm.sku_cd,
          title: this.addGoodsForm.title,
          sts_cd: this.addGoodsForm.sts_cd,
          created_by: this.addGoodsForm.created_by,
          creation_date: this.addGoodsForm.creation_date,
          upc: this.addGoodsForm.upc,
          ean: this.addGoodsForm.ean,
          name_en: this.addGoodsForm.name_en,
          ebay_description: this.addGoodsForm.ebay_description,
          amazon_description: this.addGoodsForm.amazon_description,
          key_words: this.addGoodsForm.key_words,
          width: this.addGoodsForm.width,
          height: this.addGoodsForm.height,
          weight: this.addGoodsForm.weight,
          length: this.addGoodsForm.length,
          model: this.addGoodsForm.model,
          replenishment_period: this.addGoodsForm.replenishment_period,
          warranty_day: this.addGoodsForm.warranty_day,
          remark: res.data,
          user_id: sessionStorage.getItem("user_id"),
          pro_id: this.addGoodsForm.pro_id
        }).then(res => {
          if (res.code == 504) {
            this.$message.warning(res.message);
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
          if (res.code == 200) {
            this.$message.success(res.message);
            this.refresh();
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
        });
        this.drawer = false;
        return;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // if (this.count == 0) {
          //   this.$message.warning("Please upload goods picture");
          //   return;
          // }
          if (
            this.addGoodsForm.ebay_description == "" ||
            this.addGoodsForm.amazon_description == ""
          ) {
            this.$message.warning("Please enter the description");
            return;
          }

          if (!this.isAdd && this.count == 0) {
            console.log("update");
            // console.log(this.addGoodsForm.title);
            console.log(this.addGoodsForm);
            // let title = this.addGoodsForm.title;
            // console.log(title)
            this.$post("/product/updateProduct", {
              brd_id: this.addGoodsForm.brd_id,
              retail_price: this.addGoodsForm.retail_price,
              sku_cd: this.addGoodsForm.sku_cd,
              title: this.addGoodsForm.title,
              sts_cd: this.addGoodsForm.sts_cd,
              created_by: this.addGoodsForm.created_by,
              creation_date: this.addGoodsForm.creation_date,
              upc: this.addGoodsForm.upc,
              ean: this.addGoodsForm.ean,
              name_en: this.addGoodsForm.name_en,
              ebay_description: this.addGoodsForm.ebay_description,
              amazon_description: this.addGoodsForm.amazon_description,
              key_words: this.addGoodsForm.key_words,
              width: this.addGoodsForm.width,
              height: this.addGoodsForm.height,
              weight: this.addGoodsForm.weight,
              length: this.addGoodsForm.length,
              model: this.addGoodsForm.model,
              replenishment_period: this.addGoodsForm.replenishment_period,
              warranty_day: this.addGoodsForm.warranty_day,
              remark: this.addGoodsForm.remark,
              user_id: sessionStorage.getItem("user_id"),
              pro_id: this.addGoodsForm.pro_id
            }).then(res => {
              if (res.code == 504) {
                this.$message.warning(res.message);
                this.$refs["addGoodsForm"].resetFields();
                this.addGoodsForm.ebay_description = "";
                this.addGoodsForm.amazon_description = "";
                this.count = 0;
                this.fileList = [];
                return;
              }
              if (res.code == 200) {
                this.$message.success(res.message);
                this.refresh();
                this.$refs["addGoodsForm"].resetFields();
                this.addGoodsForm.ebay_description = "";
                this.addGoodsForm.amazon_description = "";
                this.count = 0;
                this.fileList = [];
                return;
              }
            });
            this.drawer = false;
            return;
          }

          if (this.count == 0 && this.isAdd) {
            this.$message.warning("Please upload product picture");
            return;
          }
          this.$refs.upload.submit();
        } else {
          return false;
        }
      });
    },
    check() {
      console.log(this.addGoodsForm.title);
      console.log(this.addGoodsForm);
    },
    edit(row) {
      this.isAdd = false;
      this.drawer = true;
      this.$nextTick(function() {
        this.addGoodsForm = JSON.parse(JSON.stringify(row));
        // this.addGoodsForm = row;
      });

    },
    btntag(msg) {
      if (msg == "Not in warehouse") return "danger";
      if (msg == "In warehouse") return "success";
      if (msg == "On shelf") return "warning";
    },
    btn(msg) {
      if (msg == "Not in warehouse") return "Push";
      if (msg == "In warehouse") return "Shelve";
      if (msg == "On shelf") return "Unshelve";
    },
    tag(msg) {
      if (msg == "Not in warehouse") return "danger";
      if (msg == "In warehouse") return "warning";
      if (msg == "On shelf") return "success";
    },
    search() {
      this.tableData = this.table.filter(e =>
        e.title.match(this.search_goodstitle)
      );
    },
    refresh() {
      this.$post("/product/getProducts", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          this.tableData = res.data;
          return;
        }
        if (res.code == 200) {
          this.tableData = res.data;
        }
      });
    },
    operate(row) {
      let nextStage = "";
      if (row.sts_cd == "Not in warehouse") {
        nextStage = "In warehouse";
      }
      if (row.sts_cd == "In warehouse") {
        nextStage = "On shelf";
      }
      if (row.sts_cd == "On shelf") {
        nextStage = "In warehouse";
      }
      this.$post("/product/updateSts", {
        pro_id: row.pro_id,
        sts_cd: nextStage
      }).then(res => {
        // console.log(res)
        if (res.code == 504) {
          this.$message.warning(res.message);
          return;
        }
        if (res.code == 200) {
          this.refresh();
        }
      });
    }
  }
};
</script>

<style scoped>
.goods-form-button {
  margin-top: 2rem;
  margin-left: 30rem;
}
.markdown {
  margin-top: 1rem;
  height: 25rem;
  width: 50rem;
}
.flex {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.slip >>> .el-drawer__body {
  overflow-y: auto;
}
.form-div {
  margin-left: 3rem;
}
.add-goods-form {
  margin-top: 2rem;
}
.top {
  display: flex;
  flex-direction: row;
  width: 20%;
  font-size: 15px;
  font-weight: bold;
  justify-content: space-between;
}
.table-check >>> .el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #5AB9EA;
  border-color: #5AB9EA;
}
</style>