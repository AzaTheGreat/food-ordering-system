<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item>User management</el-breadcrumb-item>
    </el-breadcrumb>
    <br />
    <br />

    <span>
      Search：
      <el-input style="width:15rem" placeholder="User name" @input="search" v-model="search_name"></el-input>
    </span>
    <el-button type="primary" icon="el-icon-plus" @click="add">Add</el-button>

    <el-divider></el-divider>
    <el-table
      :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)"
      style="width: 100%"
      class="table"
      @selection-change="handleSelectionChange"
      height="550"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="username" label="User name"></el-table-column>
      <el-table-column prop="name" label="Nick name"></el-table-column>
      <el-table-column prop="role_id" label="Role"></el-table-column>
      <el-table-column prop="phone" label="Phone number"></el-table-column>
      <el-table-column prop="email" label="E-mail"></el-table-column>
      <el-table-column label="operation">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-user" @click="permissions(scope.row)"></el-button>
          <el-button type="success" size="mini" icon="el-icon-edit" @click="edit(scope.row)"></el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="remove(scope.row)"></el-button>
        </template>
      </el-table-column>
    </el-table>
    <br>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[9,12,tableData.length]"
      :page-size="pagesize"
      layout="total,sizes,prev,pager,next,jumper"
      :total="tableData.length"
    ></el-pagination>
    <br />
    <el-button size="medium" type="danger" icon="el-icon-delete" @click="removeMore">Delete</el-button>
    <el-drawer title="drawer" :visible.sync="drawer" size="35%" :with-header="false">
      <div class="form-div">
        <h3>User info</h3>
        <el-form :model="userForm" ref="userForm" label-width="130px" class="add-user-form">
          <el-form-item
            label="User name"
            prop="username"
            :rules="[{ required: true, message: 'Please enter the User name'}]"
          >
            <el-input style="width:20rem" v-model="userForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Nick name"
            prop="name"
            :rules="[{ required: true, message: 'Please enter the nickname'}]"
          >
            <el-input style="width:20rem" v-model="userForm.name" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Password"
            prop="password"
            :rules="[{ required: true, message: 'Please enter the password'}]"
          >
            <el-input
              style="width:20rem"
              v-model="userForm.password"
              autocomplete="off"
              show-password
            ></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Phone"
            prop="phone"
            :rules="[{ required: true, message: 'Please enter the phone'}]"
          >
            <el-input style="width:20rem" v-model="userForm.phone" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="E-mail"
            prop="email"
            :rules="[{ required: true, message: 'Please enter the E-mail'}]"
          >
            <el-input style="width:20rem" v-model="userForm.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item
            class="radio"
            label="Role"
            prop="role_id"
            :rules="[{ required: true, message: 'Please choose the Role'}]"
          >
            <el-radio v-model="userForm.role_id" label="MVO">Seller</el-radio>
            <el-radio v-model="userForm.role_id" label="BVO">User</el-radio>
          </el-form-item>

          <el-form-item class="user-form-button">
            <el-button type="primary" @click="submitForm('userForm')">Confirm</el-button>
            <el-button @click="cancel('userForm')">Cancel</el-button>
          </el-form-item>
          <br />
        </el-form>
      </div>
    </el-drawer>
    <!-- direction="ltr" -->
    <el-drawer title="drawer" :visible.sync="drawerPr" size="20%" :with-header="false">
      <div class="form-div">
        <h3>Permission Assignment</h3>
        <span>Admin：</span>
        <div class="switch" v-for="item in menu0" :key="item.menu_id">
          <el-switch v-model="item.state" active-color="#13ce66" inactive-color="#D8D8D8" @change="changePermission($event,item.menu_id)"></el-switch>
          <p class="p-menu">
            <i :class="item.menu_icon"></i>
            {{item.menu_name}}
          </p>
        </div>
        <span>Seller：</span>
        <div class="switch" v-for="item in menu1" :key="item.menu_id">
          <el-switch v-model="item.state" active-color="#13ce66" inactive-color="#D8D8D8" @change="changePermission($event,item.menu_id)"></el-switch>
          <p class="p-menu">
            <i :class="item.menu_icon"></i>
            {{item.menu_name}}
          </p>
        </div>
        <span>User：</span>
        <div class="switch" v-for="item in menu2" :key="item.menu_id">
          <el-switch v-model="item.state" active-color="#13ce66" inactive-color="#D8D8D8" @change="changePermission($event,item.menu_id)"></el-switch>
          <p class="p-menu">
            <i :class="item.menu_icon"></i>
            {{item.menu_name}}
          </p>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentPage: 1,
      pagesize: 9,
      menu0: [],
      menu1: [],
      menu2: [],
      beingChangedUserID:0,
      search_name: "",
      drawerPr: false,
      drawer: false,
      isAdd: false,
      tableData: [],
      table: [],
      multipleSelection: [],
      userForm: {
        user_id: "",
        username: "",
        name: "",
        password: "",
        phone: "",
        email: "",
        role_id: ""
      }
    };
  },
  mounted() {
    this.$post("/wal/getroleid", {
      user_id: sessionStorage.getItem("user_id")
    }).then(res => {
      if (res.message == "0") {
        this.getUsers();
      } else {
        this.$message.warning("Permission denied");
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
    getUsers() {
      this.$post("/rle/getUsers", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        if (res.code == 200) {
          this.tableData = res.data.user;
          this.table = res.data.user;
        } else {
          if (res.message == "Permission denied") {
            this.$message.warning("Permission denied");
          }
        }
      });
    },
    permissions(row) {
      if(row.user_id == 3){
        this.$message.warning("Can't edit the admin account");
        return
      }
      this.$post("/menuList/getAllMenusWithState", {
        user_id: row.user_id
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          return;
        }
        if (res.code == 200) {
          this.menu0 = res.data.filter(e => e.parent_id == 0);
          this.menu1 = res.data.filter(e => e.parent_id == 1);
          this.menu2 = res.data.filter(e => e.parent_id == 2);
          this.beingChangedUserID = row.user_id;
        }
      });

      this.drawerPr = true;
    },
    changePermission(state,menu_id){
      if(this.beingChangedUserID==0){
        this.$message.warning("You are changing the wrong user, confirm user again.");
      }
      if(state == true){
        this.$post("/menuList/addMenuList", {
        user_id: this.beingChangedUserID,
        menu_id: menu_id
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning("Permission modification failed, try again");
          this.drawerPr = false;
          return;
        }
        if (res.code == 200) {
          this.$message.success("Success upgrade permission of current user!");
        }
      });
      }
      if(state == false){
        this.$post("/menuList/deleteMenuList", {
        user_id: this.beingChangedUserID,
        menu_id: menu_id
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning("Permission modification failed, try again");
          this.drawerPr = false;
          return;
        }
        if (res.code == 200) {
          this.$message.success("Success degrade permission of current user!");
        }
      });
      }
      console.log(state);
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.isAdd) {
            let tmp;
            if(this.userForm.role_id=="Admin"){
              tmp = "0"
              return;
            }
            if(this.userForm.role_id=="MVO"){
              tmp = "1";
            }
            if(this.userForm.role_id=="BVO"){
              tmp = "2";
            }
            this.$post("/user/register", {
            username: this.userForm.username,
            password:this.userForm.password,
            name:this.userForm.name,
            phone:this.userForm.phone,
            email:this.userForm.email,
            role_id:tmp
          }).then(res => {
            if (res.code == 504) {
              this.$notify.error("The user name already exists");
            }
            if (res.code == 200) {
              this.getUsers();
              this.$notify.success("Registered successfully");
            }
          });
            this.isAdd = false;
          } else {
            this.$post("/rle/updateUser", {
              admin_id: sessionStorage.getItem("user_id"),
              user_id: this.userForm.user_id,
              username: this.userForm.username,
              password: this.userForm.password,
              name: this.userForm.name,
              email: this.userForm.email,
              phone: this.userForm.phone,
              role_id: this.userForm.role_id
            }).then(res => {
              if (res.code == 200) {
                this.getUsers();
                this.$message.success("Successfully update!");
              } else {
                this.$message.warning("Update failed");
              }
            });
          }
          this.drawer = false;
        } else {
          return false;
        }
      });
    },
    cancel(formName) {
      this.$refs[formName].resetFields();
      this.drawer = false;
    },
    add() {
      this.isAdd = true;
      this.userForm = [];
      this.drawer = true;
    },
    edit(row) {
      this.isAdd = false;
      this.userForm = JSON.parse(JSON.stringify(row));
      console.log(row);
      if (this.userForm.role_id == "Admin") {
        this.$message.warning("Can't edit the admin account");
        return;
      }
      this.drawer = true;
    },
    remove(row) {
       if (this.userForm.role == "Admin") {
        this.$message.warning("Can't remove the admin account");
        return;
      }
      this.$post("/rle/deletedUser", {
        user_id: sessionStorage.getItem("user_id"),
        delete_id: row.user_id
      }).then(res => {
        if (res.code == 200) {
          this.getUsers();
          this.$message.success("Delete success");
        } else {
          this.$message.warning("Delete failed");
        }
      });
      
    },
    removeMore() {
      if (this.multipleSelection.length == 0) {
        this.$message.warning("Please select item");
        return;
      }
      this.multipleSelection.forEach(element => {
        this.$post("/rle/deletedUser", {
          user_id: sessionStorage.getItem("user_id"),
          delete_id: element.user_id
        }).then(res => {
          if (res.code == 200) {
            this.getUsers();
          } else {
            this.$message.warning("Delete failed");
          }
        });
      });

      this.$message.success("Delete success");
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    search() {
      this.tableData = this.table.filter(e =>
        e.username.match(this.search_name)
      );
    }
  }
};
</script>

<style scoped>
.p-menu {
  margin-left: 1rem;
}
.switch {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.form-div {
  margin-left: 3rem;
  height: 50rem;
}
.add-user-form {
  margin-top: 5rem;
}
.user-form-button {
  margin-top: 3rem;
  margin-left: 10rem;
}
.table >>> .el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #5AB9EA;
  border-color: #5AB9EA;
}
.table >>> .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #5AB9EA;
  border-color: #5AB9EA;
}
.table >>> .el-checkbox__input.is-checked + .el-checkbox__label {
  color: #5AB9EA;
}
.radio >>> .el-radio__input.is-checked .el-radio__inner {
  border-color: #5AB9EA;
  background: #5AB9EA;
}
.radio >>> .el-radio__input.is-checked + .el-radio__label {
  color: #5AB9EA;
}
</style>