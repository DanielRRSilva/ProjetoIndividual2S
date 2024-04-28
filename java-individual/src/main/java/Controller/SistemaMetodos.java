package Controller;
import Model.Usuario;
import java.util.HashMap;
import java.util.Map;

public class SistemaMetodos {
   private Map<String, Usuario> mapaDeUsuarios = new HashMap<>();
   public void adicionarUsuarioPadrao() {
        String email = "daniel@gmail.com";
        String senha = "daniel123";
        Usuario usuario = new Usuario("Daniel", email, "11.111.111/1111-11", senha);
        mapaDeUsuarios.put(email, usuario);
   }

   public Boolean validarEmailLogin(String email) {
    for (Map.Entry<String, Usuario> usuario: mapaDeUsuarios.entrySet()) {
        if (usuario.getKey().equals(email)) {
            return true;
        }
    }
    System.out.println("Usuário não encontrado!");
    return false;
   }

   public Boolean validarSenhaLogin(String senha) {
        for (Map.Entry<String, Usuario> usuario: mapaDeUsuarios.entrySet()) {
            if (usuario.getValue().getSenha().equals(senha)) {
                return true;
            }
        }
        System.out.println("Senha inválida!");
        return false;
   }
}