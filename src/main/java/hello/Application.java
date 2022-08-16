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

  public boolean scoreFlag = false;

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
    this.r = Objects.equals(this.r, "T") ? "R" : "T";
    if(this.r.equals("T")){
      count++;
      this.r = "T";
      return "tetap T";
    }
    int score = arenaUpdate.arena.state.get(arenaUpdate._links.self.href).score;
    if(score != this.score) {
      this.scoreFlag = true;
      this.score = score;
      this.r = "T";
      return "Score Beda T";
    } else if (scoreFlag) {
      scoreFlag = false;
      this.r = "R";
      return "score sama R";
    }
    count++;
    if(count >= 4){
      count = new Random().nextInt(4);
      return "counting F";
    }
    return "pass " + this.r;
  }

}

