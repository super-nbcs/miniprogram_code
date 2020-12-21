package com.zfw.core.sys.service.impl;

import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IMenuDao;
import com.zfw.core.sys.entity.Menu;
import com.zfw.core.sys.service.IMenuService;
import com.zfw.core.sys.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends CommonServiceImpl<Menu, Integer> implements IMenuService {
    @Autowired
    private IMenuDao iMenuDao;
    @Autowired
    private IRoleMenuService iRoleMenuService;

    @Override
    public Set<Menu> findByParentId(Integer parentId) {
        return iMenuDao.findAllByParentId(parentId);
    }

    @Override
    public List<Menu> findAllByParentIdOrderBySortAsc(Integer parentId) {
        return iMenuDao.findAllByParentIdOrderBySortAsc(parentId);
    }

    @Override
    public List<Menu> getAllTreeMenus() {
        Set<Menu> rootMenus = this.findByParentId(0);
        rootMenus.forEach(menu -> {
            this.getChildren(menu);
        });
        ArrayList<Menu> menus = new ArrayList<>(rootMenus);
        return menus.stream().sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList());
    }



    /**
     * 递归查询，判断menu是否有子节点，无返回本身，有放入menu.children中
     * @param menu
     * @return
     */
    public Menu getChildren(Menu menu){
        if (menu.isHasChildren()){
            Set<Menu> childrenMenus = this.findByParentId(menu.getId());
            childrenMenus.forEach(item->{
                getChildren(item);
            });
            ArrayList<Menu> children = new ArrayList<>(childrenMenus);
            menu.setChildren(children.stream().sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()));
        }
        return menu;
    }

    @Override
    @Transactional
    public void deleteMenu(Integer id) {
        iRoleMenuService.deleteAllByMenuId(id);
        this.deleteById(id);
    }

    @Override
    public Menu findByName(String name) {
        return iMenuDao.findByName(name);
    }

}
