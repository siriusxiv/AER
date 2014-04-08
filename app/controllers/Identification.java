package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.identification;

public class Identification extends Controller {

    public static Result main() {
    	return ok(identification.render());
    }
    
    
}
