package ik.am.jpetstore.app.account;

import ik.am.jpetstore.app.account.AccountForm.EditAccount;
import ik.am.jpetstore.app.account.AccountForm.NewAccount;
import ik.am.jpetstore.domain.model.Account;
import ik.am.jpetstore.domain.model.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("account")
public class AccountController {

    private static final List<String> LANGUAGE_LIST;

    private static final List<String> CATEGORY_LIST;

    @Inject
    protected Mapper beanMapper;

    @Inject
    protected AccountHelper accountHelper;

    static {
        List<String> langList = new ArrayList<String>();
        langList.add("english");
        langList.add("japanese");
        LANGUAGE_LIST = Collections.unmodifiableList(langList);

        List<String> catList = new ArrayList<String>();
        catList.add("FISH");
        catList.add("DOGS");
        catList.add("REPTILES");
        catList.add("CATS");
        catList.add("BIRDS");
        CATEGORY_LIST = Collections.unmodifiableList(catList);
    }

    @ModelAttribute("languageList")
    public List<String> getLanguageList() {
        return LANGUAGE_LIST;
    }

    @ModelAttribute("categoryList")
    public List<String> getCategoryList() {
        return CATEGORY_LIST;
    }

    @ModelAttribute
    public AccountForm setUpForm() {
        return new AccountForm();
    }

    @RequestMapping("signonForm")
    public String signonForm() {
        return "account/SignonForm";
    }

    @RequestMapping("newAccountForm")
    public String newAccountForm() {
        return "account/NewAccountForm";
    }

    @RequestMapping("newAccount")
    public String newAccount(@Validated(NewAccount.class) AccountForm form,
            BindingResult result) {
        if (result.hasErrors()) {
            return "account/NewAccountForm";
        }
        accountHelper.newAccount(form);
        return "redirect:/account/signonForm";
    }

    @RequestMapping("editAccountForm")
    public String editAccountForm(AccountForm form) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Account account = userDetails.getAccount();
        beanMapper.map(account, form);
        form.setPassword("");
        return "account/EditAccountForm";
    }

    @RequestMapping("editAccount")
    public String editAccount(@Validated(EditAccount.class) AccountForm form,
            BindingResult result) {
        if (result.hasErrors()) {
            return "account/EditAccountForm";
        }
        accountHelper.editAccount(form);
        return "redirect:/account/editAccountForm";
    }
}