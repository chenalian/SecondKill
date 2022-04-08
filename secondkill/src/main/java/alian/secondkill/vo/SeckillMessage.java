/**
 * @program: secondkill
 * @author: alian
 * @description: 秒杀消息
 * @create: 2022-04-08 01:57
 **/

package alian.secondkill.vo;

import alian.secondkill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {

    private User tUser;

    private Long goodsId;
}