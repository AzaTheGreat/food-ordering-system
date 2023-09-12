package com.neusoft.bsp.controller;

import com.neusoft.bsp.admin.menu.mapper.MenuListMapper;
import com.neusoft.bsp.admin.menu.po.Menu;
import com.neusoft.bsp.admin.menu.po.MenuList;
import com.neusoft.bsp.admin.menu.service.MenuListService;
import com.neusoft.bsp.admin.menu.service.MenuService;
import com.neusoft.bsp.admin.menu.vo.MenuDetailWithID;
import com.neusoft.bsp.admin.menu.vo.MenuListItem;
import com.neusoft.bsp.admin.menu.vo.MenuWithIDAndState;
import com.neusoft.bsp.admin.user.po.User;
import com.neusoft.bsp.admin.user.service.UserService;
import com.neusoft.bsp.common.base.BaseController;
import com.neusoft.bsp.common.base.BaseModel;
import com.neusoft.bsp.common.base.BaseModelJson;
import com.neusoft.bsp.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/menuList")
public class MenuListController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuListService menuListService;



    @PostMapping("/getAllMenusWithState")
    public BaseModelJson<List<MenuWithIDAndState>> getAllMenusWithState(@RequestBody User us){

        BaseModelJson<List<MenuWithIDAndState>> result = new BaseModelJson<>();

        List<Menu> allMenu = menuService.getAll();
        List<MenuWithIDAndState> menuWithIDAndStates = new ArrayList<>();

        for (Menu menu : allMenu) {
            MenuWithIDAndState menuList = new MenuWithIDAndState();

            menuList.setMenu_id(menu.getMenu_id());
            menuList.setMenu_icon(menu.getMenu_icon());
            menuList.setMenu_name(menu.getMenu_name());
            menuList.setMenu_url(menu.getMenu_url());
            menuList.setParent_id(menu.getParent_id());
            MenuList menuList1 = menuListService.checkByUserAndMenuID(us.getUser_id(), menu.getMenu_id());

            if (menuList1 == null) {
                menuList.setState(false);
            } else {
                menuList.setState(true);
            }
            menuWithIDAndStates.add(menuList);
        }
        if(menuWithIDAndStates.size() != 0){
            result.setSuccess();
            result.setData(menuWithIDAndStates);
            return result;
        }
        else{
            throw BusinessException.NO_DATA;
        }
        }


    @PostMapping("/getMenuListByID")
    public BaseModelJson<List<MenuDetailWithID>> getMenuListByID(@RequestBody User us){

        BaseModelJson<List<MenuDetailWithID>> result = new BaseModelJson<>();
        List<MenuList> menus = menuListService.getListsByID(us.getUser_id());
        List<MenuDetailWithID> menuDetailWithIDS = new ArrayList<>();

        for (MenuList menuList : menus) {
            MenuDetailWithID menuDetailWithID = new MenuDetailWithID();
            Menu menu = menuService.getById(menuList.getMenu_id());
            menuDetailWithID.setMenu_id(menu.getMenu_id());
            menuDetailWithID.setMenu_icon(menu.getMenu_icon());
            menuDetailWithID.setMenu_name(menu.getMenu_name());
            menuDetailWithID.setMenu_url(menu.getMenu_url());
            menuDetailWithIDS.add(menuDetailWithID);
        }
        if(menuDetailWithIDS.size() != 0){
            result.setSuccess();
            result.setData(menuDetailWithIDS);
            return result;
        }
        else{
            throw BusinessException.NO_DATA;
        }
    }


    @PostMapping("/addMenuList")
    public BaseModel addMenuList(@RequestBody MenuListItem menuListItem) {

        BaseModel result = new BaseModel();
        MenuList menuList = new MenuList(menuListItem.getUser_id(),menuListItem.getMenu_id());
        int i = menuListService.insert(menuList);
        if(i == 1){
            result.code = 200;
            return result;
        }else {
            throw BusinessException.INSERT_FAIL;
        }
    }


    @PostMapping("/deleteMenuList")
    public BaseModel deleteMenuList(@RequestBody MenuListItem menuListItem) {

        BaseModel result = new BaseModel();
        int i = menuListService.deleteByTwoID(menuListItem.getUser_id(),menuListItem.getMenu_id());
        if(i == 1){
            result.code = 200;
            return result;
        }else {
            throw BusinessException.DELETE_FAIL;
        }
    }
}
