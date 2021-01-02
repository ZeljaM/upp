import styled from 'styled-components';

export const Container = styled.div`
  background-image: url('../img/bg01.jpg');
  width: 100%;
  height: ${window.innerHeight}px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;

  .ant-table {
    width: 85vw;
  }
`;

export const FormContainer = styled.div`
  width: 350px;
  border-radius: 15px;
  padding: 20px;
  background: #ffffff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  transition: all 0.2s ease-in-out;

  .form-button {
    margin-top: 5px;
    border-radius: 8px;
  }

  .register-text {
    color: #888888;
    margin-top: 12px;
  }

  .title {
    text-align: center;
    margin-bottom: 20px;
  }

  .register {
    font-size: 12px;
  }

  &:hover {
    cursor: pointer;
    transform: scale(1.05);
  }
`;
