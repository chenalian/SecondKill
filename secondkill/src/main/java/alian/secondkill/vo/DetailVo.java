/**
 * @program: secondkill
 * @author: alian
 * @description:
 * @create: 2022-04-07 17:00
 **/

package alian.secondkill.vo;

import alian.secondkill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private int remainSeconds;
}