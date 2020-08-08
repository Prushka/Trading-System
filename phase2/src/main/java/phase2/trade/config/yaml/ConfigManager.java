package phase2.trade.config.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import phase2.trade.user.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigManager {

    public void read() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            PermissionConfig order = mapper.readValue(new File("permission.yaml"), PermissionConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save() {
        PermissionConfig permissionConfig = new PermissionConfig();
        permissionConfig.adminPermission = new ArrayList<Permission>(){{add(Permission.ADD_ITEM);}};
        permissionConfig.regularPermission = new ArrayList<Permission>(){{add(Permission.ADD_ITEM);}};
        permissionConfig.guestPermission = new ArrayList<Permission>(){{add(Permission.ADD_ITEM);}};

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(new File("permission.yaml"), permissionConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
