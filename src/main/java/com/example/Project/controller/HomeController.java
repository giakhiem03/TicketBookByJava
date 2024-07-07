package com.example.Project.controller;

import com.example.Project.model.*;
import com.example.Project.repository.HistoryBookRepository;
import com.example.Project.repository.RoleRepository;
import com.example.Project.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    HoiThaoService hoiThaoService;
    @Autowired
    HistoryBookService historyBookService;
    @Autowired
    User_HoiThaoService userHoiThaoService;
    @Autowired
    User_GiaiDauService userGiaiDauService;
    @Autowired
    User_LiveShowService userLiveShowService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    RoleRepository roleRepository;
    @GetMapping()
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "4") int size){
        if(page<0){
            page=0;
        }
        Page<HoiThao> hoithaos = hoiThaoService.getAllforHome(page, size);
        model.addAttribute("hoithaos",hoithaos);
        model.addAttribute("totalPages", hoithaos.getTotalPages());
        return "home/home";
    }
    @GetMapping("/tick/{id}")
    public String tick(@PathVariable int id , Model model){
        if(hoiThaoService.getById(id).getCategory().getId() == 1){
            model.addAttribute("hoithao",hoiThaoService.getById(id));
            model.addAttribute("user_hoithao",new User_HoiThao());
            return "home/tickHT";
        } else if(hoiThaoService.getById(id).getCategory().getId() == 2){
            model.addAttribute("giaidau",hoiThaoService.getById(id));
            User_GiaiDau userGiaiDau = new User_GiaiDau();
            model.addAttribute("user_giaidau",userGiaiDau);
            return "home/tickGD";
        }
        model.addAttribute("liveshow",hoiThaoService.getById(id));
        User_LiveShow userLiveShow = new User_LiveShow();
        model.addAttribute("user_liveshow",userLiveShow);
        return "home/tickLS";
    }
    @PostMapping("/tickHT")
    public  String tickHT(@Valid @ModelAttribute("user_hoithao") User_HoiThao userHoiThao, BindingResult result, int id_hoiThao , Model model,
                          Principal principal){
        if(result.hasErrors()){
            model.addAttribute("hoithao",hoiThaoService.getById(id_hoiThao));
            return "home/tickHT";
        }
        var hoiThao = hoiThaoService.getById(id_hoiThao);
        var user = userService.getUserByUsername(principal.getName());
        userHoiThao.setHoiThao(hoiThao);
        userHoiThao.setUser(user);
        userHoiThaoService.save(userHoiThao);
        //add new History Book
        HistoryBook historyBook = new HistoryBook();
        historyBook.setUser(user);
        historyBook.setHoiThao(hoiThao);
        historyBookService.save(historyBook);
        return "redirect:/home";
    }

    @PostMapping("/tickGD")
    public  String tickGD(@Valid @ModelAttribute("user_giaidau") User_GiaiDau userGiaiDau, BindingResult result, int id_giaiDau , Model model,
                        Principal principal){
        if(result.hasErrors()){
            model.addAttribute("giaidau",hoiThaoService.getById(id_giaiDau));
            return "home/tickGD";
        }
        var hoiThao = hoiThaoService.getById(id_giaiDau);
        var user = userService.getUserByUsername(principal.getName());
        userGiaiDau.setHoiThao(hoiThao);
        userGiaiDau.setUser(user);
        userGiaiDauService.save(userGiaiDau);
        //add new History Book
        HistoryBook historyBook = new HistoryBook();
        historyBook.setUser(user);
        historyBook.setHoiThao(hoiThao);
        historyBookService.save(historyBook);
        return "redirect:/home";
    }

    @PostMapping("/tickLS")
    public  String tickGD(@Valid @ModelAttribute("user_liveshow") User_LiveShow userLiveShow,
                          BindingResult result, int id_liveShow , Model model,
                          Principal principal){
        if(result.hasErrors()){
            model.addAttribute("liveshow",hoiThaoService.getById(id_liveShow));
            return "home/tickGD";
        }
        var hoiThao = hoiThaoService.getById(id_liveShow);
        var user = userService.getUserByUsername(principal.getName());
        userLiveShow.setHoiThao(hoiThao);
        userLiveShow.setUser(user);
        userLiveShowService.save(userLiveShow);
        //add new History Book
        HistoryBook historyBook = new HistoryBook();
        historyBook.setUser(user);
        historyBook.setHoiThao(hoiThao);
        historyBookService.save(historyBook);
        return "redirect:/home";
    }
    @GetMapping("/addHT")
    public String addHT(Model model){
        model.addAttribute("hoithao",new HoiThao());
        model.addAttribute("categories",categoryService.getAll());
        return "home/add";
    }

    @PostMapping("/addHT")
    public String add(@Valid @ModelAttribute("hoithao") HoiThao hoiThao,BindingResult result, MultipartFile imageHoiThao,Model model){
        if(result.hasErrors()){
            model.addAttribute("categories",categoryService.getAll());
            return "home/add";
        }
        if(!imageHoiThao.isEmpty()) {
            try{
                Path path = Paths.get("static/images");
                if(!Files.exists(path)){
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageHoiThao.getOriginalFilename();
                hoiThao.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageHoiThao.getInputStream(), pathImage);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        hoiThaoService.save(hoiThao);
        return "redirect:/home";
    }

    @GetMapping("/hoithao")
    public String HoiThao(Model model,@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "4") int size){
        if(page<0){
            page=0;
        }
        model.addAttribute("hoithaos",hoiThaoService.getAllByCategory(1,page,size));
        int totalPages = hoiThaoService.getAllByCategory(1,page,size).getTotalPages();
        model.addAttribute("totalPages",totalPages);
        return "home/hoithao";
    }

    @GetMapping("/giaidau")
    public String GiaiDau(Model model,@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "4") int size){
        if(page<0){
            page=0;
        }
        model.addAttribute("giaidaus",hoiThaoService.getAllByCategory(2,page,size));
        int totalPages = hoiThaoService.getAllByCategory(2,page,size).getTotalPages();
        model.addAttribute("totalPages", totalPages);
        return "home/giaidau";
    }

    @GetMapping("/liveshow")
    public String LiveShow(Model model,@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "4") int size){
        if(page<0){
            page=0;
        }
        Page<HoiThao> liveshows = hoiThaoService.getAllByCategory(3,page, size);
        model.addAttribute("liveshows",liveshows);
        int totalPages = liveshows.getTotalPages();
        model.addAttribute("totalPages",totalPages);
        return "home/liveshow";
    }

    @GetMapping("/myTicket")
    public String myTicket(Model model,Principal principal,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "4") int size){

        model.addAttribute("myTickets",historyBookService.getAllByUsername(principal.getName(),page,size));
        model.addAttribute("totalPages",historyBookService.getAllByUsername(principal.getName(),page,size).getTotalPages());
        return "home/myTicket";
    }

    @GetMapping("/changePW")
    public String changePW( ){
        return "home/changePW";
    }

    @PostMapping("/changePW")
    public String changePW(@RequestParam("old_password") String old_password,@RequestParam("new_password") String new_password,
                           @RequestParam("repassword") String repassword,Principal principal,Model model){
        boolean check = false;
        if(old_password.isEmpty()){
            model.addAttribute("old_password","vui lòng nhập mật khẩu hiện tại!");
            check = true;
        }
        if(repassword.isEmpty()){
            model.addAttribute("repassword","vui lòng nhập lại mật khẩu!");
            check = true;
        }
        if( new_password.isEmpty()){
            model.addAttribute("new_password","vui lòng nhập mật khẩu mới!");
            check = true;
        }
        if(check){
            return "home/changePW";
        }
        if(!new_password.equals(repassword)){
            model.addAttribute("errorRePW","mật khẩu không khớp! vui lòng nhập lại!");
            return "home/changePW";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userService.getAll().stream().filter(p->p.getUserName().equals(principal.getName()))
                .findFirst().orElse(null);
        if(user!=null && passwordEncoder.matches(old_password,user.getPassword())){
            String new_passwords = new BCryptPasswordEncoder().encode(new_password);
            user.setPassword(new_passwords);
            userService.save(user);
            return "redirect:/home";
        }
        model.addAttribute("wrongPW","Sai mật khẩu!");
        return "home/changePW";
    }

    @GetMapping("/searchHT")
    public String SearchHT(Model model,@RequestParam int checkPrice,
                           @RequestParam(defaultValue = "0") int pageNo,
                           @RequestParam(defaultValue = "4") int pageSize){
        if(pageNo<0){
            pageNo=0;
        }
        Page<HoiThao> list;
        if (checkPrice == 0) {
            list = hoiThaoService.findByCategoryAndPriceEquals(1, 0,pageNo, pageSize);
        } else {
            list = hoiThaoService.findByCategoryAndPriceNotEquals(1, 0, pageNo, pageSize);
        }
        model.addAttribute("hoithaos",list);
        model.addAttribute("totalPages", list.getTotalPages());
        return "home/hoithao";
    }

    @GetMapping("/searchGD")
    public String SearchGD(Model model,@RequestParam int checkPrice,
                           @RequestParam(defaultValue = "0") int pageNo,
                           @RequestParam(defaultValue = "4") int pageSize){
        if(pageNo<0){
            pageNo=0;
        }
        Page<HoiThao> list;
        if (checkPrice == 0) {
            list = hoiThaoService.findByCategoryAndPriceEquals(2, 0,pageNo, pageSize);
        } else {
            list = hoiThaoService.findByCategoryAndPriceNotEquals(2, 0, pageNo, pageSize);
        }
        model.addAttribute("giaidaus",list);
        model.addAttribute("totalPages", list.getTotalPages());
        return "home/giaidau";
    }

    @GetMapping("/searchLS")
    public String SearchLS(Model model,@RequestParam int checkPrice,
                           @RequestParam(defaultValue = "0") int pageNo,
                           @RequestParam(defaultValue = "4") int pageSize){
        if(pageNo<0){
            pageNo=0;
        }
        Page<HoiThao> list;
        if (checkPrice == 0) {
           list = hoiThaoService.findByCategoryAndPriceEquals(3, 0,pageNo, pageSize);

        } else {
            list = hoiThaoService.findByCategoryAndPriceNotEquals(3, 0, pageNo, pageSize);
        }
        model.addAttribute("liveshows",list);
        model.addAttribute("totalPages", list.getTotalPages());
        return "home/liveshow";
    }
    @GetMapping("/searchHome")
    public String SearchHome(Model model,@RequestParam String title,
                             @RequestParam(defaultValue = "0") int pageNo,
                             @RequestParam(defaultValue = "4") int pageSize){
        if(pageNo<0){
            pageNo=0;
        }
        model.addAttribute("title",title);
        Page<HoiThao> hoithaos = hoiThaoService.searchByTitle(title , pageNo, pageSize);
        int totalPages = hoithaos.getTotalPages();
        model.addAttribute("hoithaos",hoithaos);
        model.addAttribute("totalPages", totalPages);
        return "home/searchHome";
    }

    @GetMapping("/qlUser")
    public String UserManagement(Model model){
        model.addAttribute("users",userService.getAll());
        return "home/UserManagement";
    }
    @GetMapping("/qlHT")
    public String QLHT(Model model, @RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "4") int pageSize){
        model.addAttribute("hoithaos",hoiThaoService.getAllByCategory(1,pageNo,pageSize));
        model.addAttribute("totalPages", hoiThaoService.getAllByCategory(1,pageNo,pageSize).getTotalPages());
        return "home/qlHT";
    }
    @GetMapping("/qlGD")
    public String QLGD(Model model, @RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "4") int pageSize){
        model.addAttribute("giaidaus",hoiThaoService.getAllByCategory(2,pageNo,pageSize));
        model.addAttribute("totalPages", hoiThaoService.getAllByCategory(2,pageNo,pageSize).getTotalPages());
        return "home/qlGD";
    }
    @GetMapping("/qlLS")
    public String QLLS(Model model, @RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "4") int pageSize){
        model.addAttribute("liveshows",hoiThaoService.getAllByCategory(3,pageNo,pageSize));
        model.addAttribute("totalPages", hoiThaoService.getAllByCategory(3,pageNo,pageSize).getTotalPages());
        return "home/qlLS";
    }

    @GetMapping("/updateUser/{userName}")
    public String Update(Model model,@PathVariable String userName){
        model.addAttribute("user", userService.getUserByUsername(userName));
        model.addAttribute("roles",roleRepository.findAll());
        return "home/updateProfileUser";
    }

    @PostMapping("/updateUser")
    public String Update(@ModelAttribute User user,@RequestParam int roles, MultipartFile imageUser, String imageDefault) {
        if (!imageUser.isEmpty()) {
            try {
                Path path = Paths.get("static/images");
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageUser.getOriginalFilename();
                user.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageUser.getInputStream(), pathImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            user.setImage(imageDefault);
        }
        Set<Role> roleSet = new HashSet<>();
        Role role = roleRepository.findById(roles).orElseThrow();
        roleSet.add(role);
        user.setRoles(roleSet);
        userService.save(user);
        return "redirect:/home/qlUser";
    }

    @GetMapping("/delete/{userName}")
    public String deleteUser(Model model,@PathVariable String userName){
        User user = userService.getUserByUsername(userName);
        userService.delete(user);
        return "redirect:/home/qlUser";
    }

    @GetMapping("updateHT/{id}")
    public String updateHT(Model model,@PathVariable int id){
        model.addAttribute("hoithao",hoiThaoService.getById(id));
        model.addAttribute("categories",categoryService.getAll());
        String formattedDateTime = hoiThaoService.getById(id).getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        model.addAttribute("formattedDateTime", formattedDateTime);
        return "/home/updateHT";
    }
    @PostMapping("updateHT")
    public String updateHT(@Valid @ModelAttribute("hoithao") HoiThao hoiThao,BindingResult result,
                           @RequestParam String time, MultipartFile imageHoiThao,Model model){
        HoiThao hoiThao_update = hoiThaoService.getById(hoiThao.getId());
        if(result.hasErrors()){
            model.addAttribute("categories",categoryService.getAll());
            return "home/updateHT";
        }
        if(!imageHoiThao.isEmpty()) {
            try{
                Path path = Paths.get("static/images");
                if(!Files.exists(path)){
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageHoiThao.getOriginalFilename();
                hoiThao_update.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageHoiThao.getInputStream(), pathImage);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        LocalDateTime dateTime = null;
        dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        hoiThao_update.setTime(dateTime);
        hoiThao_update.setTitle(hoiThao.getTitle());
        hoiThao_update.setDetail(hoiThao.getDetail());
        hoiThao_update.setPrice(hoiThao.getPrice());
        hoiThao_update.setAddress(hoiThao.getAddress());
        hoiThao_update.setSeatNumber(hoiThao.getSeatNumber());
        hoiThao_update.setCategory(hoiThao.getCategory());
        hoiThaoService.save(hoiThao_update);
        return "redirect:/home/qlHT";
    }
    @GetMapping("/deleteHT/{id}")
    public String deleteHT(Model model,@PathVariable int id){
        hoiThaoService.delete(hoiThaoService.getById(id));
        return "redirect:/home/qlHT";
    }

    @GetMapping("updateGD/{id}")
    public String updateGD(Model model,@PathVariable int id){
        model.addAttribute("giaidau",hoiThaoService.getById(id));
        model.addAttribute("categories",categoryService.getAll());
        String formattedDateTime = hoiThaoService.getById(id).getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        model.addAttribute("formattedDateTime", formattedDateTime);
        return "/home/updateGD";
    }
    @PostMapping("updateGD")
    public String updateGD(@Valid @ModelAttribute("giaidau") HoiThao hoiThao,BindingResult result,
                           @RequestParam String time, MultipartFile imageHoiThao,Model model){

        HoiThao hoiThao_update = hoiThaoService.getById(hoiThao.getId());
        if(result.hasErrors()){
            model.addAttribute("categories",categoryService.getAll());
            return "home/updateGD";
        }
        if(!imageHoiThao.isEmpty()) {
            try{
                Path path = Paths.get("static/images");
                if(!Files.exists(path)){
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageHoiThao.getOriginalFilename();
                hoiThao_update.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageHoiThao.getInputStream(), pathImage);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        LocalDateTime dateTime = null;
        dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        hoiThao.setTime(dateTime);
        hoiThao_update.setTitle(hoiThao.getTitle());
        hoiThao_update.setDetail(hoiThao.getDetail());
        hoiThao_update.setPrice(hoiThao.getPrice());
        hoiThao_update.setAddress(hoiThao.getAddress());
        hoiThao_update.setSeatNumber(hoiThao.getSeatNumber());
        hoiThao_update.setCategory(hoiThao.getCategory());
        hoiThaoService.save(hoiThao_update);
        return "redirect:/home/qlGD";
    }
    @GetMapping("/deleteGD/{id}")
    public String deleteGD(Model model,@PathVariable int id){
        hoiThaoService.delete(hoiThaoService.getById(id));
        return "redirect:/home/qlGD";
    }


    @GetMapping("updateLS/{id}")
    public String updateLS(Model model,@PathVariable int id){
        model.addAttribute("liveshow",hoiThaoService.getById(id));
        model.addAttribute("categories",categoryService.getAll());
        String formattedDateTime = hoiThaoService.getById(id).getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        model.addAttribute("formattedDateTime", formattedDateTime);
        return "/home/updateLS";
    }
    @PostMapping("updateLS")
    public String updateLS(@Valid @ModelAttribute("liveshow") HoiThao hoiThao,BindingResult result,
                           @RequestParam String time, MultipartFile imageHoiThao,Model model){
        HoiThao hoiThao_update = hoiThaoService.getById(hoiThao.getId());
        if(result.hasErrors()){
            model.addAttribute("categories",categoryService.getAll());
            return "home/updateLS";
        }
        if(!imageHoiThao.isEmpty()) {
            try{
                Path path = Paths.get("static/images");
                if(!Files.exists(path)){
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageHoiThao.getOriginalFilename();
                hoiThao_update.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageHoiThao.getInputStream(), pathImage);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        LocalDateTime dateTime = null;
        dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        hoiThao_update.setTime(dateTime);
        hoiThao_update.setTitle(hoiThao.getTitle());
        hoiThao_update.setDetail(hoiThao.getDetail());
        hoiThao_update.setPrice(hoiThao.getPrice());
        hoiThao_update.setAddress(hoiThao.getAddress());
        hoiThao_update.setSeatNumber(hoiThao.getSeatNumber());
        hoiThao_update.setCategory(hoiThao.getCategory());
        hoiThaoService.save(hoiThao_update);
        return "redirect:/home/qlLS";
    }
    @GetMapping("/deleteLS/{id}")
    public String deleteLS(Model model,@PathVariable int id){
        hoiThaoService.delete(hoiThaoService.getById(id));
        return "redirect:/home/qlLS";
    }

    @GetMapping("/qlDLHT")
    public String QLDLHT(Model model,@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "4") int size){
        model.addAttribute("hoithaos",userHoiThaoService.getAll(page,size));
        model.addAttribute("totalPages",userHoiThaoService.getAll(page,size).getTotalPages());
        return "home/qlDLHT";
    }

    @GetMapping("/qlDLGD")
    public String QLDLGD(Model model,@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "4") int size){
        model.addAttribute("giaidaus",userGiaiDauService.getAll(page,size));
        model.addAttribute("totalPages",userGiaiDauService.getAll(page,size).getTotalPages());
        return "home/qlDLGD";
    }

    @GetMapping("/qlDLLS")
    public String QLDLLS(Model model,@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "4") int size){
        model.addAttribute("liveshows",userLiveShowService.getAll(page,size));
        model.addAttribute("totalPages",userLiveShowService.getAll(page,size).getTotalPages());
        return "home/qlDLLS";
    }
}
