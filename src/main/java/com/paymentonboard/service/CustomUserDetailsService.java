package com.paymentonboard.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymentonboard.dto.AuditUser;
import com.paymentonboard.dto.UserRequest;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRequest user1;

    @Autowired
    private UserRequest user2;
    @Autowired
    private CommonService CommonService;

    @Autowired
    private JdbcTemplate JdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleMasterRepository roleMasterRepository;

    @Autowired
    MerchantUserUserGroupMpgRepository merchantUserUserGroupMpgRepository;

    @Autowired
    MerchantUserGroupMenuMpgRepository merchantUserGroupMenuMpgRepository;

    @Autowired
    MerchantUserGroupMstRepository merchantUserGroupMstRepository;

    @Autowired
    MenuMstRepository menuMstRepository;

    @Autowired
    MenuPermissionRepository menuPermissionRepository;

    @Autowired
    MerchantRepository merchantRepository;

    private static final String MERCHANT_ROLE_ID = "2";
    private static final String INVOICE_MANAGEMENT_MENU_ID = "112";
    private static final String USER_MANAGEMENT_MENU_ID = "201";



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            user1.setUsername(username);
            user2 = CommonService.userLoginDetails(user1);

            if (username != null) {
                return new User(username, user2.getPassword(), new ArrayList<>());
            } else {
                throw new UsernameNotFoundException("User not found!!!");
            }
        } catch (Exception e) {
            log.error("Error :: {0}", e);
        }
        return null;

    }

    public AuditUser loadUserByUsernameCustom(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with user Id: " + userId));
        RoleMaster roleMaster = roleMasterRepository.findByRoleId(user.getRoleId());
        return new AuditUser(user.getUserId(), roleMaster.getRoleName());
    }

    @SuppressWarnings("deprecation")
    public String getMenuHierarchy(String roleId) throws Exception {
        DataSource connection = null;
        CallableStatement callableStatement = null;
        ResultSet rs = null;
        String str = null;
        Connection cn = null;

        try {


            connection = JdbcTemplate.getDataSource();
            cn = connection.getConnection();

            if (cn != null) {
                callableStatement = cn.prepareCall("{call pro_getRoleMenuPermissionV2(?)}");
                callableStatement.setString(1, roleId);
                callableStatement.execute();
                rs = callableStatement.getResultSet();
            }

            //initiaze
            Map<String, List<Map<Object, Object>>> map = new LinkedHashMap<>();//new HashMap<String, List<Map<Object,Object>>>();
            Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
            Map<String, List<String>> menuMap1 = new HashMap<String, List<String>>();
            int i = 0;
            while (rs.next()) {
                i++;
                List<Map<Object, Object>> list = new ArrayList<>();

                LinkedHashMap<Object, Object> pr = new LinkedHashMap<>(); //initiaze
                LinkedHashMap<Object, Object> lhm = new LinkedHashMap<>();
                String menu = rs.getString("MenuName");
                String subMenu = rs.getString("SubmenuName");
                String link = rs.getString("MenuLink");
                String permission = rs.getString("PermissionAction");
                pr.put("permissions", permission);
                lhm.put("Permission", pr);

                lhm.put("submenu", subMenu);
                lhm.put("link", link);
                lhm.put("Parents_Id", rs.getString("MenuId"));
                lhm.put("subMenu_Id", rs.getString("submenuid"));
                if (map.containsKey(menu))  //check if mainmenu already present
                {
                    map.get(menu).add(lhm);
                } else {
                    //initialize list here

                    list.add(lhm);
                    map.put(menu, list);
                }

            }
            Gson gson = new Gson();
            str = gson.toJson(map);

            log.info("ParmVlaues::::::::::::::: {}", str);
            cn.close();

        } catch (Exception e1) {
            throw new Exception();
        } finally {
            cn.close();


            JdbcTemplate.getDataSource().getConnection().close();


            log.info("finally block is always executed");
        }
        return str;

    }


    ///////////
    public ArrayList getMenusHirarchy(String roleId, String merchantId, String userMerchantId) throws Exception {
        List<SqlParameter> prmtrsList = new ArrayList<>();
        prmtrsList.add(new SqlParameter(Types.VARCHAR));

        try {
            MerchantUserUserGroupMpgEntity userAndUserGroupEntity = merchantUserUserGroupMpgRepository.findByUserIdAndUserGroupMstIdInAndIsActive(userMerchantId, merchantId, Boolean.TRUE);
            if (merchantId != null && !merchantId.equalsIgnoreCase(userMerchantId) && userAndUserGroupEntity != null && roleId.equalsIgnoreCase("2")) {
                List<MerchantUserGroupMenuMpgEntity> merchantUserGroupMenuMpgEntities = merchantUserGroupMenuMpgRepository
                        .findByUserGroupMstId(userAndUserGroupEntity.getUserGroupMst().getId());
                List<MenuDto> menuList = new ArrayList<>();
                for (MerchantUserGroupMenuMpgEntity merchantUserGroupMenuMpgEntity : merchantUserGroupMenuMpgEntities) {
                    MenuMstEntity menuMstEntity = menuMstRepository.findByMenuId(merchantUserGroupMenuMpgEntity.getMenuId());
                    if (menuMstEntity != null) {
                        MenuDto menuDto = prepareMenuDto(merchantUserGroupMenuMpgEntity, menuMstEntity);
                        menuList.add(menuDto);
                    }
                }

                List<Map<String, Object>> menuResponseArrayList = new ArrayList<>();
                for (MenuDto menuDto : menuList) {
                    Map<String, Object> menuResponseMap = getRawMenuDataResponse(menuDto);
                    menuResponseArrayList.add(menuResponseMap);
                }
                return (ArrayList) menuResponseArrayList;
            } else {
                log.info("ParmVlaues::::::::::::::: {}", prmtrsList);
                Map<String, Object> resultData = JdbcTemplate.call(connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{call pro_getRoleMenuPermissionV3(?,?)}");
                    callableStatement.setString(1, roleId);
                    callableStatement.setString(2, merchantId == null ? userMerchantId : merchantId);

                    return callableStatement;
                }, prmtrsList);
                ArrayList arrayList = (ArrayList) resultData.get("#result-set-1");

                if (MERCHANT_ROLE_ID.equalsIgnoreCase(roleId)) {
                    MerchantList merchant = merchantRepository.findByMerchantIdAndIsDeleted(merchantId, "N");
                    boolean ibpsFlag = merchant.getIbpsEmailNotification().equals("1");
                    if (!ibpsFlag) {
                        Iterator<Map<String, Object>> itr = arrayList.iterator();
                        while (itr.hasNext()) {
                            Map<String, Object> menu = itr.next();
                            if (INVOICE_MANAGEMENT_MENU_ID.equalsIgnoreCase(menu.get("PMenuId").toString())
                                    || INVOICE_MANAGEMENT_MENU_ID.equalsIgnoreCase(menu.get("MenuId").toString()))
                                itr.remove();
                        }
                    }

                    boolean parentFlag = merchant.isParentMerchant();
                    if (!parentFlag) {
                        Iterator<Map<String, Object>> itr = arrayList.iterator();
                        while (itr.hasNext()) {
                            Map<String, Object> menu = itr.next();
                            if (USER_MANAGEMENT_MENU_ID.equalsIgnoreCase(menu.get("PMenuId").toString())
                                    || USER_MANAGEMENT_MENU_ID.equalsIgnoreCase(menu.get("MenuId").toString()))
                                itr.remove();
                        }
                    }
                }

                if (arrayList.isEmpty()) {
                    arrayList.add("Fields does not exist");
                    return arrayList;
                }
                return arrayList;
            }
        } catch (Exception e1) {
            log.error("Error in GetDetailsApi :: {0}", e1);
            throw e1;
        } finally {
            Objects.requireNonNull(JdbcTemplate.getDataSource()).getConnection().close();
            log.info("finally block is always executed");
        }
    }

    public Map<String, Object> getRawMenuDataResponse(MenuDto menuDto) {
        Map<String, Object> menuResponseMap = new HashMap<>();
        menuResponseMap.put("MenuId", menuDto.getMenuId());
        menuResponseMap.put("MenuName", menuDto.getMenuName());
        menuResponseMap.put("MenuLink", menuDto.getMenuLink());
        menuResponseMap.put("Icon", menuDto.getIcon());
        menuResponseMap.put("PMenuId", menuDto.getPMenuId());
        menuResponseMap.put("permissionID", menuDto.getPermissionId());
        menuResponseMap.put("PermissionAction", menuDto.getPermissionAction());
        menuResponseMap.put("Position", menuDto.getPosition());
        menuResponseMap.put("Title", menuDto.getTitle());
        menuResponseMap.put("roleId", menuDto.getRoleId());
        menuResponseMap.put("ROLENAME", menuDto.getRoleName());
        return menuResponseMap;
    }

    private MenuDto prepareMenuDto(MerchantUserGroupMenuMpgEntity merchantUserGroupMenuMpgEntity, MenuMstEntity menuMstEntity) {
        MenuDto menuDto = new MenuDto();
        menuDto.setMenuId(merchantUserGroupMenuMpgEntity.getMenuId());
        menuDto.setMenuName(menuMstEntity.getMenuName());
        menuDto.setMenuLink(menuMstEntity.getMenuLink());
        menuDto.setPMenuId(menuMstEntity.getPMenuId());
        menuDto.setPosition(menuMstEntity.getPosition());
        menuDto.setTitle(menuMstEntity.getTitle());
        menuDto.setIcon(menuMstEntity.getIcon());
        setMenuPermissions(menuMstEntity.getMenuId(), menuDto);
        menuDto.setRoleId("2");
        menuDto.setRoleName("Merchant");
        return menuDto;
    }

    private void setMenuPermissions(int menuId, MenuDto menuDto) {
        List<MenuPermissionEntity> permissionList = menuPermissionRepository.findByMenuId(menuId);
        String permissionActions = permissionList.stream().map(MenuPermissionEntity::getPermissionAction).collect(Collectors.joining(","));
        List<Integer> permissionIds = permissionList.stream().map(MenuPermissionEntity::getPermissionId).collect(Collectors.toList());
        String permissionIdsString = permissionIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        menuDto.setPermissionAction(permissionActions);
        menuDto.setPermissionId(permissionIdsString);
    }

    public int CheckPermission(String roleId, String permission) throws Exception {
        // TODO Auto-generated method stub
        List<SqlParameter> prmtrsList = new ArrayList<SqlParameter>();
        prmtrsList.add(new SqlParameter(Types.VARCHAR));
        prmtrsList.add(new SqlParameter(Types.VARCHAR));


        try {
            log.info("ParmVlaues::::::::::::::: {}", prmtrsList);
            Map<String, Object> resultData = JdbcTemplate.call(connection -> {
                CallableStatement callableStatement = connection.prepareCall("{call pro_checkPermission(?,?)}");
                callableStatement.setString(1, roleId);
                callableStatement.setString(2, permission);
                return callableStatement;
            }, prmtrsList);
            ArrayList arrayList = new ArrayList();
            arrayList = (ArrayList) resultData.get("#result-set-1");
            if (arrayList.isEmpty()) {
                arrayList.add("Fields does not exist");
                return 0;
            }

            Gson gson = new Gson();
            String jsonArray = gson.toJson(arrayList);
            JSONArray Jr = new JSONArray(arrayList);
            int i = Jr.getJSONObject(0).getInt("totalcount");

            log.info("CheckPermissionStatus::::: {} ", i);

            return i;
        } catch (Exception e1) {
            throw new Exception();
        }

    }
}
