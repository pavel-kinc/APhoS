package cz.muni.aphos.controllers;

import cz.muni.aphos.dao.*;
import cz.muni.aphos.dto.UploadLog;
import cz.muni.aphos.dto.User;
import cz.muni.aphos.exceptions.UnauthorizedAccessException;
import cz.muni.aphos.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * The Profile controller handles profile page displaying, changing the user info,
 * and creating new users.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    UploadLogsDao uploadLogsDao;

    @Autowired
    UploadErrorMessagesDao uploadErrorMessagesDao;

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    /**
     * Display profile page of the given user, allow editing if it is the profile of
     * the user who made the request.
     *
     * @param id       the user id
     * @param editable true if the editable version of the profile is to be displayed
     * @param model    the model
     * @return the profile.html or the editableProfile.html templates
     */
    @GetMapping("/")
    public String displayProfile(@RequestParam(defaultValue = "current") String id,
                                 @RequestParam(defaultValue = "false") Boolean editable, Model model) {
        if (id.equals("current")) {
            id = userService.getCurrentUser().getGoogleSub();
        }
        User user = userRepo.findByUserID(id);
        if (editable && !user.getGoogleSub().equals(userService.getCurrentUser().getGoogleSub())) {
            throw new UnauthorizedAccessException("Not authorized to access.");
        }
        if (id.equals(userService.getCurrentUser().getGoogleSub())) {
            List<UploadLog> uploads = uploadLogsDao.getLogsForUser(id);
            for (UploadLog uploadLog : uploads) {
                uploadLog.setFileErrorMessagePairsList(
                        uploadErrorMessagesDao.getMessagesForLog(uploadLog.getId()));
            }
            model.addAttribute("userUploads", uploads);
            model.addAttribute("currentUserSignedIn", true);
        }
        model.addAttribute("user", user);
        return editable ? "editableProfile" : "profile";
    }

    /**
     * Post request to save the profile info to the database after changing it.
     *
     * @param description the profile description
     * @param username    the username
     * @return the redirect to the profile of the user who made the request
     */
    @PostMapping("/save")
    public String saveProfile(@RequestParam String description, @RequestParam String username) {
        User user = userService.getCurrentUser();
        user.setDescription(description);
        if (userRepo.existsByUsernameEquals(username) && !username.equals(user.getUsername())) {
            userRepo.save(user);
            return "redirect:/profile/?id=" + user.getGoogleSub() + "&editable=true";
        }
        user.setUsername(username);
        userRepo.save(user);
        return "redirect:/profile/?id=" + user.getGoogleSub();
    }

    /**
     * Display the new user form.
     *
     * @return the usernameForm.html template
     */
    @GetMapping("/username")
    public String displayUsernameForm() {
        return "usernameForm";
    }

    /**
     * Save username new user to the database.
     *
     * @param username           the username
     * @param redirectAttributes the redirect attributes
     * @return redirects back to the username form if the name is taken, profile otherwise
     */
    @PostMapping("/username/save")
    public String saveUsername(@RequestParam String username, RedirectAttributes redirectAttributes) {
        if (userRepo.existsByUsernameEquals(username)) {
            redirectAttributes.addFlashAttribute("message", "Username already taken");
            return "redirect:/profile/username";
        }
        User user = userService.getCurrentUser();
        user.setUsername(username);
        userRepo.save(user);
        log.info("New user created with username " + username);
        return "redirect:/profile/?id=" + user.getGoogleSub();
    }

}
