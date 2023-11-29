

@Controller
@RequestMapping("/enroll")
public class EnrollController {
    @Autowired
    PUserSessionSpring pUserSessionSpring;

    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public String showTerms(Model model, HttpServletRequest request) {
        if (pUserSessionSpring.getPUserSession().getEnrollProgress() == null
                || !pUserSessionSpring.getPUserSession().getEnrollProgress().isValidate()) {
            model.addAttribute("errorMsg",
                    messageResolver.getLocalizedMessage(MSG_ERROR, LocalContextHolder.getLocale()));
        }

        if (pUserSessionSpring != null && pUserSessionSpring.getPUserSession() != null
                && pUserSessionSpring.getPUserSession().getProvider() != null) {
            model.addAttribute("facilityList",
                    pUserSessionSpring.getPUserSession().getProvider().getFacilityList());
        }

        return "OK";
    }
}
