package com.project.marketplace;

import com.github.javafaker.Faker;
import com.project.marketplace.dto.*;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.*;
import com.project.marketplace.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class MarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(
			CustomerRepository customerRepo,
			SellerRepository sellerRepo,
			ProductService productService,
			VoucherService voucherService,
			DiscountService discountService,
			ProductRepository productRepo,
			CustomerService customerService,
			SellerService sellerService,
			UserRepository userRepository
	) {
		return args -> {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

			Address address1 = new Address();
			address1.setStreet("123 Green St");
			address1.setCity("Eco City");
			address1.setState("Sustainable State");
			address1.setPostalCode("54321");
			address1.setAdditionalInfo("Near eco park");

			Address address2 = new Address();
			address2.setStreet("123 Green St");
			address2.setCity("123 City");
			address2.setState("Sustainable State");
			address2.setPostalCode("54321");
			address2.setAdditionalInfo("Block A");

			sellerService.register(new SellerRequestDto("john_doe", "secure123", "johndoe@example.com", "1233789512", UserRole.SELLER, "Doe company"));

			Seller seller1 = sellerRepo.findById(1).get();
			seller1.setAddress(address1);
			sellerRepo.save(seller1);

			customerService.register(new CustomerRequestDto("david", "password", "one@gmail.com", "0165597831", UserRole.CUSTOMER));

			Customer customer1 = customerRepo.findById(2).get();
			customer1.setAddress(address2);
			customer1.setImage("Camera.jpg");
			customerRepo.save(customer1);

			sellerService.register(new SellerRequestDto("jane", "jane123", "jane@gmail.com", "0189647261", UserRole.SELLER, "JaneCompany"));

			Seller seller2 = sellerRepo.findById(3).get();
			seller2.setAddress(address2);
			sellerRepo.save(seller2);

			sellerService.register(new SellerRequestDto("alice", "alice123", "alice@gmail.com", "0158932157", UserRole.SELLER, "aliceCompany"));

			Seller seller3 = sellerRepo.findById(4).get();
			seller3.setAddress(address1);
			sellerRepo.save(seller3);

			customerService.register(new CustomerRequestDto("Cust", "q123", "Cust@gmail.com", "1234567890", UserRole.CUSTOMER));

			Customer customer2 = customerRepo.findById(5).get();
			customer2.setAddress(address1);
			customerRepo.save(customer2);

			Admin admin = new Admin();
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPhone("0126987453");
			admin.setRole(UserRole.ADMIN);
			admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
			userRepository.save(admin);

			User admin1 = userRepository.findById(6).get();
			admin1.setAddress(address2);
			userRepository.save(admin1);

			productService.addProduct(new ProductRequestDto(
					"Reusable Bamboo Cutlery Set",
					"Eco-friendly bamboo fork, spoon, knife, chopsticks, and straw with a portable cloth pouch for zero-waste meals on the go.",
					new BigDecimal(12.99),
					"Bamboo-Cutlery-Set.jpg",
					150,
					ProductCategory.Kitchen,
					new BigDecimal(0.10),
					"Bamboo, Cotton Pouch",
					"FDA Approved",
					5,
					1
			));

			Product product1 = productRepo.findById(1).get();
			product1.setStatus(ProductStatus.APPROVED);
			productRepo.save(product1);

			productService.addProduct(new ProductRequestDto(
					"Organic Cotton Tote Bag",
					"Durable, reusable, and biodegradable tote bag made from 100% organic cotton. Ideal for groceries or daily use.",
					new BigDecimal(8.99),
					"Organic-Tote-Bag.jpg",
					250,
					ProductCategory.Fashion,
					new BigDecimal(0.05),
					"Organic Cotton",
					"GOTS Certified",
					8,
					1
			));

			Product product2 = productRepo.findById(2).get();
			product2.setStatus(ProductStatus.APPROVED);
			productRepo.save(product2);

			productService.addProduct(new ProductRequestDto(
					"Natural Loofah Bath Sponge",
					"Biodegradable bath sponge made from dried loofah plant for gentle exfoliation and plastic-free skincare.",
					new BigDecimal(5.49),
					"Natural-Loofah.jpg",
					180,
					ProductCategory.Health,
					new BigDecimal(0.03),
					"Natural Loofah Plant",
					"Compostable",
					5,
					1
			));

			Product product3 = productRepo.findById(3).get();
			product3.setStatus(ProductStatus.APPROVED);
			productRepo.save(product3);

			productService.addProduct(new ProductRequestDto(
					"Compostable Pet Waste Bags",
					"Made from cornstarch and biodegradable polymers, these pet waste bags decompose naturally without harming the environment.",
					new BigDecimal(9.99),
					"Compostable-Pet-Waste-Bags.jpg",
					120,
					ProductCategory.PetProduct,
					new BigDecimal(0.08),
					"Cornstarch, PBAT",
					"EN 13432 Certified",
					3,
					1
			));

			Product product4 = productRepo.findById(4).get();
			product4.setStatus(ProductStatus.APPROVED);
			productRepo.save(product4);

			productService.addProduct(new ProductRequestDto(
					"Wheat Straw Travel Toothbrush",
					"A lightweight, biodegradable toothbrush made with wheat straw and soft BPA-free bristles, perfect for travel.",
					new BigDecimal(3.99),
					"Wheat-Straw-Toothbrush.jpg",
					200,
					ProductCategory.Travel,
					new BigDecimal(0.02),
					"Wheat Straw, Nylon Bristles",
					"FDA Approved",
					10,
					1
			));

			Product product5 = productRepo.findById(5).get();
			product5.setStatus(ProductStatus.APPROVED);
			productRepo.save(product5);

			productService.addProduct(new ProductRequestDto(
					"Recycled Paper Notebook",
					"Eco-conscious notebook made with 100% post-consumer recycled paper and soy-based ink for sustainable writing.",
					new BigDecimal(6.49),
					"Recycled-Notebook.jpg",
					220,
					ProductCategory.Office,
					new BigDecimal(0.04),
					"Recycled Paper, Cardboard Cover",
					"FSC Certified",
					6,
					1
			));

			Product product6 = productRepo.findById(6).get();
			product6.setStatus(ProductStatus.APPROVED);
			productRepo.save(product6);

			productService.addProduct(new ProductRequestDto(
					"Reusable Beeswax Food Wraps",
					"Sustainable alternative to plastic wrap, made with organic cotton infused with beeswax, jojoba oil, and tree resin.",
					new BigDecimal(14.99),
					"Beeswax-Wraps.jpg",
					130,
					ProductCategory.Kitchen,
					new BigDecimal(0.12),
					"Organic Cotton, Beeswax, Tree Resin",
					"FDA Approved",
					15,
					1
			));

			Product product7 = productRepo.findById(7).get();
			product7.setStatus(ProductStatus.APPROVED);
			productRepo.save(product7);

			productService.addProduct(new ProductRequestDto(
					"Cork Yoga Mat",
					"Natural cork surface with a rubber backing for a non-toxic, eco-friendly yoga experience.",
					new BigDecimal(39.99),
					"Cork-Yoga-Mat.jpg",
					80,
					ProductCategory.Health,
					new BigDecimal(0.30),
					"Natural Cork, Natural Rubber",
					"REACH Compliant",
					3,
					1
			));

			productService.addProduct(new ProductRequestDto(
					"Hemp Travel Pouch",
					"Sturdy travel pouch made from natural hemp fibers, perfect for organizing personal items in a sustainable way.",
					new BigDecimal(10.49),
					"Hemp-Travel-Pouch.jpg",
					170,
					ProductCategory.Travel,
					new BigDecimal(0.06),
					"Hemp Fabric, Metal Zipper",
					"OEKO-TEX Certified",
					9,
					1
			));

			productService.addProduct(new ProductRequestDto(
					"Pet Rope Toy from Recycled Cotton",
					"Durable and safe rope toy for pets made from recycled cotton. Great for chewing and tug-of-war.",
					new BigDecimal(7.99),
					"Recycled-Cotton-Pet-Toy.jpg",
					140,
					ProductCategory.PetProduct,
					new BigDecimal(0.07),
					"Recycled Cotton",
					"ASTM F963 Certified",
					12,
					1
			));

			productService.addProduct(new ProductRequestDto(
					"Plant-Based Reusable Food Storage Bags",
					"Leakproof, washable storage bags made from plant-based materials for storing snacks, fruits, and leftovers.",
					new BigDecimal(13.99),
					"Plant-Based-Storage-Bags.jpg",
					160,
					ProductCategory.Kitchen,
					new BigDecimal(0.09),
					"PLA, PBAT",
					"FDA Approved",
					7,
					3
			));

			Product product11 = productRepo.findById(11).get();
			product11.setStatus(ProductStatus.APPROVED);
			productRepo.save(product11);

			productService.addProduct(new ProductRequestDto(
					"Recycled Fabric Lanyard",
					"Eco-conscious lanyard made from 100% recycled polyester, perfect for office IDs and event passes.",
					new BigDecimal(3.49),
					"Recycled-Lanyard.jpg",
					190,
					ProductCategory.Office,
					new BigDecimal(0.02),
					"Recycled Polyester",
					"GRS Certified",
					16,
					3
			));

			Product product12 = productRepo.findById(12).get();
			product12.setStatus(ProductStatus.APPROVED);
			productRepo.save(product12);

			productService.addProduct(new ProductRequestDto(
					"Organic Cotton Face Mask",
					"Soft, breathable, and washable face mask made from organic cotton, suitable for sensitive skin.",
					new BigDecimal(6.99),
					"Organic-Face-Mask.jpg",
					140,
					ProductCategory.Health,
					new BigDecimal(0.03),
					"Organic Cotton",
					"OEKO-TEX Certified",
					8,
					3
			));

			Product product13 = productRepo.findById(13).get();
			product13.setStatus(ProductStatus.APPROVED);
			productRepo.save(product13);

			productService.addProduct(new ProductRequestDto(
					"Biodegradable Dog Grooming Brush",
					"Ergonomic grooming brush made from sustainable bamboo and natural bristles for pet care.",
					new BigDecimal(11.99),
					"Eco-Dog-Brush.jpg",
					120,
					ProductCategory.PetProduct,
					new BigDecimal(0.06),
					"Bamboo, Natural Fiber Bristles",
					"PET-Safe Certified",
					21,
					3
			));

			productService.addProduct(new ProductRequestDto(
					"Upcycled Denim Crossbody Bag",
					"Trendy and sustainable bag crafted from upcycled denim scraps, offering a second life to discarded jeans.",
					new BigDecimal(21.99),
					"Upcycled-Denim-Bag.jpg",
					100,
					ProductCategory.Fashion,
					new BigDecimal(0.15),
					"Upcycled Denim, Cotton Strap",
					"Handmade, No Certification",
					3,
					3
			));

			productService.addProduct(new ProductRequestDto(
					"Compostable Plant Fiber Plates",
					"Set of sturdy, compostable plates made from wheat bran fiber, perfect for eco-conscious picnics or parties.",
					new BigDecimal(15.49),
					"Plant-Fiber-Plates.jpg",
					200,
					ProductCategory.Kitchen,
					new BigDecimal(0.10),
					"Wheat Bran Fiber",
					"EN 13432 Certified",
					10,
					4
			));

			Product product16 = productRepo.findById(16).get();
			product16.setStatus(ProductStatus.APPROVED);
			productRepo.save(product16);

			productService.addProduct(new ProductRequestDto(
					"Bamboo Office Desk Organizer",
					"Stylish, sustainable desk organizer with compartments for stationery, paper clips, and devices.",
					new BigDecimal(17.99),
					"Bamboo-Desk-Organizer.jpg",
					110,
					ProductCategory.Office,
					new BigDecimal(0.12),
					"Bamboo Wood",
					"FSC Certified",
					11,
					4
			));

			Product product17 = productRepo.findById(17).get();
			product17.setStatus(ProductStatus.APPROVED);
			productRepo.save(product17);

			productService.addProduct(new ProductRequestDto(
					"Natural Cork Passport Holder",
					"Durable, lightweight passport holder made from eco-friendly cork leather, perfect for travelers.",
					new BigDecimal(14.99),
					"Cork-Passport-Holder.jpg",
					90,
					ProductCategory.Travel,
					new BigDecimal(0.09),
					"Natural Cork Fabric",
					"PETA-Approved Vegan",
					15,
					4
			));

			productService.addProduct(new ProductRequestDto(
					"Reusable Organic Cotton Tea Bags",
					"Eco-friendly alternative to disposable tea bags, these washable cotton bags let you brew loose tea sustainably.",
					new BigDecimal(7.49),
					"Reusable-Tea-Bags.jpg",
					210,
					ProductCategory.Kitchen,
					new BigDecimal(0.04),
					"Organic Cotton",
					"GOTS Certified",
					20,
					4
			));

			productService.addProduct(new ProductRequestDto(
					"Biodegradable Hair Ties",
					"Hair ties made from natural rubber and organic cotton, free from synthetic plastics and dyes.",
					new BigDecimal(5.99),
					"Biodegradable-Hair-Ties.jpg",
					180,
					ProductCategory.Fashion,
					new BigDecimal(0.02),
					"Organic Cotton, Natural Rubber",
					"Plastic-Free Certified",
					17,
					4
			));


			voucherService.addVoucher(new VoucherRequestDto(
					"Fashion Discount",
					"Get 20% off on all fashion items",
					LocalDate.now(),
					LocalDate.now().plusDays(32),
					new BigDecimal(3),
					ProductCategory.Fashion,
					100
			));

			voucherService.addVoucher(new VoucherRequestDto(
					"Kitchen Sale",
					"Save 15% on selected kitchen products",
					LocalDate.now(),
					LocalDate.now().plusDays(45),
					new BigDecimal(5),
					ProductCategory.Kitchen,
					200
			));

			voucherService.addVoucher(new VoucherRequestDto(
					"Travel Voucher",
					"Get $50 off on your next travel booking",
					LocalDate.now(),
					LocalDate.now().plusDays(60),
					new BigDecimal(6),
					ProductCategory.Travel,
					150
			));

			voucherService.addVoucher(new VoucherRequestDto(
					"Health Discount",
					"Get 25% off on all health-related products",
					LocalDate.now(),
					LocalDate.now().plusDays(30),
					new BigDecimal(2),
					ProductCategory.Health,
					120
			));

			voucherService.addVoucher(new VoucherRequestDto(
					"Office Savings",
					"Save $10 on selected pet products",
					LocalDate.now(),
					LocalDate.now().plusDays(30),
					new BigDecimal(3.5),
					ProductCategory.Office,
					1
			));

			discountService.addDiscount(new DiscountRequestDto(
					"Summer Sale",
					"Get amazing discounts on summer collection!",
					new BigDecimal("10.00"),
					LocalDate.of(2025, 7, 10),
					LocalDate.of(2025, 7, 20),
					1
			));

			discountService.addDiscount(new DiscountRequestDto(
					"Back to School",
					"Exclusive offers for students and teachers.",
					new BigDecimal("8.00"),
					LocalDate.of(2025, 8, 1),
					LocalDate.of(2025, 8, 15),
					1
			));

			discountService.addDiscount(new DiscountRequestDto(
					"Clearance",
					"Massive discounts on clearance items.",
					new BigDecimal("5.00"),
					LocalDate.of(2025, 9, 5),
					LocalDate.of(2025, 9, 30),
					1
			));

		};
	}

}
