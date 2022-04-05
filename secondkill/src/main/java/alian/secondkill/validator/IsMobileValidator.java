/**
 * @program: secondkill
 * @author: alian
 * @description:
 * @create: 2022-04-05 21:15
 **/

package alian.secondkill.validator;

import alian.secondkill.util.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
         required = constraintAnnotation.required();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
         if (required){
             return ValidatorUtil.isMobile(value);
       }else {
             if (StringUtils.isEmpty(value)){
                  return true;
             }else {
                  return ValidatorUtil.isMobile(value);
             }
       }
    }
}
