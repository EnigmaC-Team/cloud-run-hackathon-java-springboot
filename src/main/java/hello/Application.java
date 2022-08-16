package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  public String r = "R";
  public int score = 0;

  public boolean hitFlagDone = false;

  public boolean wasHitFlag = false;
  public int count = 0;

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
//    System.out.println(arenaUpdate);
//    String[] commands = new String[]{"F", "R", "L", "T"};
    PlayerState playerState = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    this.r = Objects.equals(this.r, "T") ? "R" : "T";

    if(playerState.score > this.score) {
      this.hitFlagDone = true;
      this.score = playerState.score;
      this.r = "T";
      return "kita hit T";
    } else if (hitFlagDone) {
      hitFlagDone = false;
      this.r = "R";
      return "hit selesai R";
    } else if(wasHitFlag){
      wasHitFlag = false;
      return "maju setelah kena hit F";
    } else if (playerState.wasHit || playerState.score < this.score) {
      wasHitFlag = true;
      this.score = playerState.score;
      this.r = "R";
      return "kita kena hit R";
    } else if(count >= 4){
      count = new Random().nextInt(4);
      return "ini countdown F";
    } else if(this.r.equals("R")){
      count++;
    }
    return this.r;
  }

}

