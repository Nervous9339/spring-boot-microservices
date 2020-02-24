package ru.nothingtdh.photoapp.api.users.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nothingtdh.photoapp.api.users.data.AlbumsServiceClient;
import ru.nothingtdh.photoapp.api.users.data.UserEntity;
import ru.nothingtdh.photoapp.api.users.data.UserRepository;
import ru.nothingtdh.photoapp.api.users.shared.UserDto;
import ru.nothingtdh.photoapp.api.users.ui.model.AlbumResponseModel;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AlbumsServiceClient albumsServiceClient;
    private final Environment environment;

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        usersRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);

        if (userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true, true, true, true,
                emptyList());
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = usersRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        List<AlbumResponseModel> albums = albumsServiceClient.getAlbums(userId);
        userDto.setAlbums(albums);
        return userDto;
    }
}

