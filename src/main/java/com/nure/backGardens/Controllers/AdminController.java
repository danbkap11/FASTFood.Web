package com.nure.backGardens.Controllers;

import com.google.gson.JsonObject;
import com.nure.backGardens.Models.UserRequest;
import com.nure.backGardens.config.jwt.JwtProvider;
import com.nure.backGardens.entites.UserEntity;
import com.nure.backGardens.service.RequestService;
import com.nure.backGardens.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@CrossOrigin(origins = "*", allowedHeaders = {"*"} )
@RestController
public class AdminController {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;


    @GetMapping("/isAdmin")
    public JsonObject registerUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(token.substring(7)));
        if(user.getRoleEntity().getId() == 1)
            return requestService.returnInfo(true, "ADMIN");
        return requestService.returnInfo(false, "NOT ADMIN");
    }

    @GetMapping("/backup")
    public boolean backup() throws IOException, InterruptedException {
        execute();
        return false;
    }

    public String execute() {
        String nameForDump = "";
        try {
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

            nameForDump = "database" + formmat1.format(LocalDateTime.now()) + ".sql";
            nameForDump = nameForDump.replaceAll(" ", "");
            System.out.println(nameForDump);
            String command = "cmd /c C:\\Users\\nqwen\\OneDrive\\mysqldump -uroot -p!Nikkolay2012 gardensdb2 > " + nameForDump;
            Process process = Runtime.getRuntime().exec(command);
            System.out.println("MYSQL automatic backup is completed, the backup time is: " + new java.util.Date().getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nameForDump;
    }


    @GetMapping("/backupAdmin")
    @ApiOperation(
            value = "Performs data backup and returns mysql dump file",
            nickname = "getBackupData"
    )
    public ResponseEntity<?> getBackupData() throws IOException, URISyntaxException {

        File file = new File("D:\\Univer\\3.2\\apz\\kursach\\backGardens\\" + execute());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=backup_data.sql");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        InputStreamResource resource =
                new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

}
