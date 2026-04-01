@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin registerAdmin(Admin admin) {
        if(adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Admin email already exists");
        }
        admin.setRole("SUPER_ADMIN"); // Default role
        return adminRepository.save(admin);
    }

    public Admin loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid Admin credentials");
        }
        return admin;
    }

    public String forgotPassword(String email, String newPassword) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin email not found"));
        
        admin.setPassword(newPassword);
        adminRepository.save(admin);
        return "Password updated successfully";
    }
}